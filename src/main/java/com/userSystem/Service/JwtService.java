package com.userSystem.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "your_secret_key"; // 替換為實際的密鑰

    public String generateJwtToken(String claimName, String identifier) {
        long EXPIRATION_TIME = 1800000; //0.5hours
        EXPIRATION_TIME=81000000;
        // Use your JWT library (e.g., Auth0 Java JWT) to create a token with the user identifier
        // Example using Auth0 Java JWT:
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create().withSubject("user")
                .withClaim(claimName, identifier)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }
    public DecodedJWT verifyJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            return null; // 驗證失敗
        }
    }
}
