package com.develop.web_server.ui.controller;

import com.develop.web_server.service.impl.UserServiceImpl;
import com.develop.web_server.shared.dto.UserDto;
import com.develop.web_server.ui.model.reponse.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {


    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;

    private UserDto userDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userDto = new UserDto();
        userDto.setId(10L);
        userDto.setFirstName("Alden");
        userDto.setPassword("1234");
        userDto.setEncryptedPassword("dd123l123");
        userDto.setUserID("l12312000f0sdf");
    }

    @Test
    void getUser() {

        when(userService.getUserByUserId(anyString())).thenReturn(userDto);
        UserRest userRest = userController.getUser("l12312000f0sdf");
        assertNotNull(userRest);
        assertEquals("l12312000f0sdf", userRest.getUserID());
        assertEquals("Alden", userRest.getFirstName());

    }
}