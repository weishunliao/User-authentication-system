package com.develop.web_server.service.impl;

import com.develop.web_server.repository.UserRepository;
import com.develop.web_server.io.entity.UserEntity;
import com.develop.web_server.service.UserService;
import com.develop.web_server.shared.Utils;
import com.develop.web_server.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Override
    public UserDto createUser(UserDto userDto) {

        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new RuntimeException("User already exist");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        userEntity.setEncryptedPassword("1lj3123123");
        userEntity.setUserID(utils.generateUserID(30));

        UserEntity storeUserDetails = userRepository.save(userEntity);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storeUserDetails, returnValue);


        return returnValue;
    }
}
