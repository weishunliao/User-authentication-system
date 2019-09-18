package com.develop.web_server.ui.controller;


import com.develop.web_server.exception.UserServiceException;
import com.develop.web_server.service.UserService;
import com.develop.web_server.shared.dto.AddressDto;
import com.develop.web_server.shared.dto.UserDto;
import com.develop.web_server.ui.model.reponse.*;
import com.develop.web_server.ui.model.request.UserDetailsRequestModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping(path = "/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest getUser(@PathVariable String userId) {
        UserRest response = new UserRest();
        UserDto userDto = userService.getUserByUserId(userId);
        BeanUtils.copyProperties(userDto, response);
        return response;
    }

    @GetMapping(path = "/{userId}/addresses",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<AddressResponseModel> getUserAddresses(@PathVariable String userId) {

        List<AddressResponseModel> response = new ArrayList<>();
        List<AddressDto> addresses = userService.getUserAddresses(userId);
        ModelMapper modelMapper = new ModelMapper();
        for (AddressDto address : addresses) {
            response.add(modelMapper.map(address,AddressResponseModel.class));
        }
        return response;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {

        if (page > 0) {
            page -= 1;
        }

        List<UserRest> response = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);
        for (UserDto user : users) {
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(user, userRest);
            response.add(userRest);
        }
        return response;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

        UserRest response;

        if (userDetails.getFirstName() == null || userDetails.getFirstName().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        UserDto createdUser = userService.createUser(userDto);
        response = modelMapper.map(createdUser, UserRest.class);

        return response;
    }

    @PutMapping(path = "/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest response = new UserRest();
        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(userDetails, userDto);
        UserDto updatedUser = userService.updateUser(userId, userDto);
        BeanUtils.copyProperties(updatedUser, response);
        return response;
    }

    @DeleteMapping(path = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel delUser(@PathVariable String userId) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(userId);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        System.out.println(returnValue);
        return returnValue;
    }
}
