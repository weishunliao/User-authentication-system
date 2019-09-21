package com.develop.web_server.shared;

import com.develop.web_server.security.SecurityConstants;
import com.mysql.cj.exceptions.ExceptionFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.ZeroCopyHttpOutputMessage;
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

        for (int i = 0; i < length; i++) {
            returnVal.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return returnVal.toString();
    }

    public boolean hasTokenExpired(String token) {
        try {

            Claims claims = Jwts.parser().setSigningKey(SecurityConstants.TOKEN_SECRET).parseClaimsJws(token).getBody();
            Date tokenExpirationDate = claims.getExpiration();
            Date today = new Date();
            return tokenExpirationDate.before(today);

        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public String generateEmailVerificationToken(String publicUserId) {
        String token = Jwts.builder().setSubject(publicUserId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .compact();
        return token;
    }

    public String generatePasswordResetToken(String userId) {
        String token = Jwts.builder().setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .compact();
        return token;
    }
}
