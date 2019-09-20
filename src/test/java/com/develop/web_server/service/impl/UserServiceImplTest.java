package com.develop.web_server.service.impl;

import com.develop.web_server.io.entity.UserEntity;
import com.develop.web_server.io.repository.UserRepository;
import com.develop.web_server.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(10L);
        userEntity.setFirstName("Alden");
        userEntity.setEncryptedPassword("dd123l123");
        userEntity.setUserID("l12312000f0sdf");
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

}