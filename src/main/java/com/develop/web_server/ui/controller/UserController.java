package com.develop.web_server.ui.controller;


import com.develop.web_server.service.UserService;
import com.develop.web_server.shared.dto.UserDto;
import com.develop.web_server.ui.model.reponse.UserRest;
import com.develop.web_server.ui.model.request.UserDetailsRequestModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping(path = "/{id}")
    public UserRest getUser(@PathVariable String id) {
        UserRest response = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, response);
        return response;
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRest response = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);
        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, response);
        return response;
    }

    @PutMapping
    public String updateUser() {
        return "update user was called";
    }

    @DeleteMapping
    public String delUser() {
        return "delete user was called";
    }
}
