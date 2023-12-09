package com.capybarainc.BookStore.Methods;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Verify {
    public static boolean VerifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256("test");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("BookStore")
                .build();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
