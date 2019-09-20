package com.develop.web_server.service.impl;

import com.develop.web_server.exception.UserServiceException;
import com.develop.web_server.io.entity.AddressEntity;
import com.develop.web_server.io.entity.PasswordEntity;
import com.develop.web_server.io.repository.AddressRepository;
import com.develop.web_server.io.repository.PasswordRepository;
import com.develop.web_server.io.repository.UserRepository;
import com.develop.web_server.io.entity.UserEntity;
import com.develop.web_server.service.UserService;
import com.develop.web_server.shared.AmazonSES;
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
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PasswordRepository passwordRepository;

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
        String publicUserId = utils.generateUserID(30);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUserID(publicUserId);
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);

        UserEntity storedUserDetails = userRepository.save(userEntity);
        UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);


        new AmazonSES().verifyEmail(returnValue);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new User(userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                userEntity.getEmailVerificationStatus(),
                true, true,
                true, new ArrayList<>());

//        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
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

    @Override
    public boolean verifyEmailToken(String token) {

        UserEntity user = userRepository.findUserByEmailVerificationToken(token);
        if (user != null) {
            boolean isExpired = utils.hasTokenExpired(token);
            if (!isExpired) {
                user.setEmailVerificationToken(null);
                user.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean passwordReset(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            String token = utils.generatePasswordResetToken(userEntity.getUserID());
            PasswordEntity passwordEntity = new PasswordEntity();
            passwordEntity.setUserDetails(userEntity);
            passwordEntity.setToken(token);
            passwordRepository.save(passwordEntity);

            boolean result = new AmazonSES().passwordReset(token);
            return result;
        }


        return false;
    }

    @Override
    public boolean setUpNewPassword(String token, String oldPassword, String newPassword) {

        PasswordEntity passwordEntity = passwordRepository.findByToken(token);
        if (passwordEntity != null) {
            UserEntity userEntity = userRepository.findById(passwordEntity.getUserDetails().getId());
            boolean isExpired = utils.hasTokenExpired(token);
            boolean matches = bCryptPasswordEncoder.matches(oldPassword, userEntity.getEncryptedPassword());
            if (!isExpired && matches) {
                userEntity.setPassword(newPassword);
                userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(newPassword));
                userRepository.save(userEntity);
                return true;
            }
        }
        return false;
    }
}
