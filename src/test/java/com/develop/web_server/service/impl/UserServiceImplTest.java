package com.develop.web_server.service.impl;

import com.develop.web_server.exception.UserServiceException;
import com.develop.web_server.io.entity.AddressEntity;
import com.develop.web_server.io.entity.UserEntity;
import com.develop.web_server.io.repository.AddressRepository;
import com.develop.web_server.io.repository.PasswordRepository;
import com.develop.web_server.io.repository.UserRepository;
import com.develop.web_server.shared.AmazonSES;
import com.develop.web_server.shared.Utils;
import com.develop.web_server.shared.dto.AddressDto;
import com.develop.web_server.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Any;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    AddressRepository addressRepository;

    @Mock
    PasswordRepository passwordRepository;

    @Mock
    Utils utils;

    @Mock
    AmazonSES amazonSES;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userEntity = new UserEntity();
        userEntity.setId(10L);
        userEntity.setFirstName("Alden");
        userEntity.setPassword("1234");
        userEntity.setEncryptedPassword("dd123l123");
        userEntity.setUserID("l12312000f0sdf");
        userEntity.setEmail("weishun.liao002@gmail.com");
        userEntity.setEmailVerificationToken("jnjk34njrkn3j4n34jnr34nrkj34nrjk34nrjk43nr");
        userEntity.setAddresses(getAddressesEntities());
    }



    @Test
    void getUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.com");
        assertNotNull(userDto);
        assertEquals("Alden",userDto.getFirstName());
    }

    @Test
    void getUser_UserNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUser("test@test.com");
        });
    }


    @Test
    void createUser() {

        when(utils.generateUserID(anyInt())).thenReturn(userEntity.getUserID());
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("357838534");
        when(utils.generateAddressID(anyInt())).thenReturn("1231mkm9090");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));

        assertThrows(RuntimeException.class, () -> {
            userService.createUser(new UserDto());
        });

        UserDto userDto = new UserDto();
        userDto.setPassword("1234");
        userDto.setAddresses(getAddresses());

        UserDto user = userService.createUser(userDto);
        assertNotNull(user);
        assertEquals(userEntity.getUserID(),user.getUserID());
        assertEquals(userEntity.getFirstName(),user.getFirstName());
        assertNotNull(user.getUserID());
        assertEquals(userEntity.getAddresses().size(),user.getAddresses().size());
        verify(utils, times(2)).generateAddressID(30);
        verify(bCryptPasswordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }


    @Test
    void createUser_Exception() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDto userDto = new UserDto();
        userDto.setEmail("test");
        assertThrows(UserServiceException.class, () -> {
            userService.createUser(userDto);
        });

    }

    private List<AddressDto> getAddresses() {
        ArrayList<AddressDto> list = new ArrayList<>();
        AddressDto addressDto1 = new AddressDto();
        AddressDto addressDto2 = new AddressDto();
        addressDto1.setType("Shipping");
        addressDto2.setType("billing");
        list.add(addressDto1);
        list.add(addressDto2);
        return list;

    }

    private List<AddressEntity> getAddressesEntities() {
        List<AddressEntity> addressEntities = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (AddressDto address : getAddresses()) {
            AddressEntity entity = modelMapper.map(address, AddressEntity.class);
            addressEntities.add(entity);
        }
        return addressEntities;

    }
}