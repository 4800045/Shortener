package com.Shortener.security;

import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JWTUtil {
    
    @Value("${JWT_secret}")
    private String secret;
    
    public String generateToken(String username) {
	Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());
	
	System.out.println("generating token started");
	
	return JWT.create()
		.withSubject("User info")
		.withClaim("Username", username)
		.withIssuedAt(new Date())
		.withExpiresAt(expirationDate)
		.withIssuer("Shortener")
		.sign(Algorithm.HMAC256(secret));
    }
    
    public String validateTokenAndRetrieveClaim(String token) {
	System.out.println("We are in token validtion method");
	
	JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
		.withSubject("User info")
		.withIssuer("Shortener")
		.build();
	
	DecodedJWT jwt = verifier.verify(token);
	
	return jwt.getClaim("Username").asString();
	
    }

}
