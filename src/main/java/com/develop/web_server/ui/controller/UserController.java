package com.develop.web_server.ui.controller;


import com.develop.web_server.exception.UserServiceException;
import com.develop.web_server.service.AddressService;
import com.develop.web_server.service.UserService;
import com.develop.web_server.shared.dto.AddressDto;
import com.develop.web_server.shared.dto.UserDto;
import com.develop.web_server.ui.model.reponse.*;
import com.develop.web_server.ui.model.request.UserDetailsRequestModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @GetMapping(path = "/{userId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest getUser(@PathVariable String userId) {
        UserRest response = new UserRest();
        UserDto userDto = userService.getUserByUserId(userId);
        BeanUtils.copyProperties(userDto, response);
        return response;
    }

    @GetMapping(path = "/{userId}/addresses",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,"application/hal+json"})
    public Resources<AddressRest> getUserAddresses(@PathVariable String userId) {

        List<AddressRest> response = new ArrayList<>();
        List<AddressDto> addresses = userService.getUserAddresses(userId);
        if (addresses != null) {
            ModelMapper modelMapper = new ModelMapper();
            for (AddressDto address : addresses) {

                Link addressLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withSelfRel();
                AddressRest addressRest = modelMapper.map(address, AddressRest.class);
                addressRest.add(addressLink);
                response.add(addressRest);
            }
        }
        return new Resources<>(response);
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,"application/hal+json"})
    public Resource<AddressRest> getUserAddressesById(@PathVariable String userId, @PathVariable String addressId) {

        AddressDto address = addressService.getAddressByAddressId(addressId);
        if (address != null) {
            ModelMapper modelMapper = new ModelMapper();
            AddressRest responseModel = modelMapper.map(address, AddressRest.class);
//            Link addressLink = linkTo(UserController.class).slash(userId).slash("addresses").slash(addressId).withSelfRel();
            Link addressLink = linkTo(methodOn(UserController.class).getUserAddressesById(userId,addressId)).withSelfRel();
            Link userLink = linkTo(UserController.class).slash(userId).withRel("user");
            Link addressesLink = linkTo(UserController.class).slash(userId).slash("addresses").withRel("address list");

            responseModel.add(addressLink);
            responseModel.add(userLink);
            responseModel.add(addressesLink);
            return new Resource<>(responseModel);
        }
        throw new UserServiceException(ErrorMessages.NO_ADDRESS_FOUND.getErrorMessage());

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
        return returnValue;
    }

    @GetMapping(path = "/email/verification")
    public OperationStatusModel verificationEmailToken(@RequestParam(value = "token") String token) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.VERIFIY_EMAIL.name());
        boolean isVerified = userService.verifyEmailToken(token);

        if (isVerified) {
            operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
        } else {
            operationStatusModel.setOperationResult(RequestOperationStatus.ERROR.name());

        }

        return operationStatusModel;
    }
}
