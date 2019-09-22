package com.develop.web_server.ui.controller;


import com.develop.web_server.ui.model.request.LoginRequestModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {


    @ApiOperation("User Login")
    @ApiResponses(value = {
            @ApiResponse(code = 200,
                    message = "Response Headers",
                    responseHeaders = {
                            @ResponseHeader(name = "authentication",
                                    description = "Bearer <Token value here>",
                                    response = String.class),
                            @ResponseHeader(name = "userid",
                                    description = "<Public User Id here>",
                                    response = String.class)
                    }
            )
    })

    @PostMapping("/users/login")
    public void loginPoint(@RequestBody LoginRequestModel loginRequestModel) {
        throw new IllegalStateException("This method is protected by Spring security.");
    }
}
