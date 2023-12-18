package potatoparoto.MovieStore.Methods;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import potatoparoto.MovieStore.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public boolean VerifyTokenWithClaim(String token, String claim, String expectedValue) {
        JWTVerifier verifier = userService.GetVerifier();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim(claim).asString().equals(expectedValue);
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
        }
        return false;
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
