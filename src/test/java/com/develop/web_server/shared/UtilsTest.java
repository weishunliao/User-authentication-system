package com.develop.web_server.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    Utils utils;


    @BeforeEach
    void setUp() {
    }

    @Test
    void generateUserID() {
        String userID = utils.generateUserID(30);
        String userID2 = utils.generateUserID(30);
        assertNotNull(userID);
        assertEquals(30, userID.length());
        assertFalse(userID.equalsIgnoreCase(userID2));

    }


    @Test
    void hasTokenNotExpired() {
        String token = utils.generateEmailVerificationToken("abc");
        assertNotNull(token);
        boolean expired = utils.hasTokenExpired(token);
        assertFalse(expired);
    }

    @Test
    void hasTokenExpired() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmMiLCJleHAiOjE1NjkwNzc1MTJ9.6nLg0eirux8H_0kpkYOBSP2nOdw8PaJugCmLwm6AfgU00GETxI4Vp9lh48qrLZN5Ibzbj60F50cRrQwcsJgP0Q";
        boolean expired = utils.hasTokenExpired(token);
        assertTrue(expired);
    }
}