package com.develop.web_server.service;

import com.develop.web_server.shared.dto.AddressDto;
import com.develop.web_server.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUser(String email);

    UserDto getUserByUserId(String userId);

    UserDto updateUser(String userId, UserDto userDto);

    void deleteUser(String userId);

    List<UserDto> getUsers(int page, int limit);

    List<AddressDto> getUserAddresses(String id);

    boolean verifyEmailToken(String token);

    UserDetails loadUserByUsername(String email);
}
