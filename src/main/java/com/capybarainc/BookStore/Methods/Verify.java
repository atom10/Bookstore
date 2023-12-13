package com.capybarainc.BookStore.Methods;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.capybarainc.BookStore.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class Verify {
    @Autowired
    UserService userService;
    public boolean VerifyToken(String token) {
        JWTVerifier verifier = userService.GetVerifier();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public String GetLogin(String token) {
        JWTVerifier verifier = userService.GetVerifier();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            System.out.println("Found login in bearer: " + decodedJWT.getClaim("login").asString());
            return decodedJWT.getClaim("login").asString();
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
