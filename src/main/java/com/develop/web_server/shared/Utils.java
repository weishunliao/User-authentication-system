package com.develop.web_server.shared;

import com.develop.web_server.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;


@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz";
//    private final int ITERATIONS = 1000;
//    private final int KEY_LENGTH = 256;

    public String generateUserID(int length) {
        return generateRandomString(length);
    }

    public String generateAddressID(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder returnVal = new StringBuilder(length);

        for (int i = 0; i <length; i++) {
            returnVal.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return returnVal.toString();
    }

    public boolean hasTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(SecurityConstants.TOKEN_SECRET).parseClaimsJws(token).getBody();
        System.out.println(claims);
        Date tokenExpirationDate = claims.getExpiration();
        Date today = new Date();

        return tokenExpirationDate.before(today);
    }

    public String generateEmailVerificationToken(String publicUserId) {
        String token = Jwts.builder().setSubject(publicUserId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPERATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .compact();
        return token;
    }
}
