package com.develop.web_server.service.impl;

import com.develop.web_server.exception.UserServiceException;
import com.develop.web_server.io.entity.AddressEntity;
import com.develop.web_server.io.repository.AddressRepository;
import com.develop.web_server.io.repository.UserRepository;
import com.develop.web_server.io.entity.UserEntity;
import com.develop.web_server.service.UserService;
import com.develop.web_server.shared.Utils;
import com.develop.web_server.shared.dto.AddressDto;
import com.develop.web_server.shared.dto.UserDto;
import com.develop.web_server.ui.model.reponse.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new RuntimeException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        }

        for (AddressDto addressDto : userDto.getAddresses()) {
            addressDto.setUserDetails(userDto);
            addressDto.setAddressId(utils.generateAddressID(30));
        }


        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUserID(utils.generateUserID(30));


        UserEntity storedUserDetails = userRepository.save(userEntity);
        UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);


        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUser(String email) {

        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> response = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, limit);
        Page<UserEntity> pageUserEntities = userRepository.findAll(pageable);
        List<UserEntity> userEntities = pageUserEntities.getContent();

        for (UserEntity userEntity : userEntities) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            response.add(userDto);
        }

        return response;
    }

    @Override
    public List<AddressDto> getUserAddresses(String id) {

        UserEntity userEntity = userRepository.findByUserID(id);

        List<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
        List<AddressDto> resp = new ArrayList<>();

        ModelMapper mapper = new ModelMapper();
        for (AddressEntity address : addresses) {
            AddressDto addressDto = mapper.map(address, AddressDto.class);
            resp.add(addressDto);
        }
        return resp;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserID(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException(userId);
        }
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserID(userId);
        if (userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());


        UserEntity updatedUserEntity = userRepository.save(userEntity);

        BeanUtils.copyProperties(updatedUserEntity, returnValue);
        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntity = userRepository.findByUserID(userId);

        if (userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userRepository.delete(userEntity);

    }
}
