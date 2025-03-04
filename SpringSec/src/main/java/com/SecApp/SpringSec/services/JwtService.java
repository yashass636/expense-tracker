package com.SecApp.SpringSec.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*
*
* Purpose of this class:
* - creates JWT token if refresh token is valid.
* - also provides methods to extract various data like expiration date, username.
*
*   Ref : https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-token-structure (structure of JWT)
*
* Claims are nothing but the entity attribute (stored as a key-value pair) which user prefers to store in payload of JWT token.
*
* */
@Service
public class JwtService {

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
    public static final long EXPIRATION_TIME = 1000*60*60*10; //10 hrs

    //extracts username from 'token'
    public String extractUsername (String token) {

        return extractClaim(token, Claims::getSubject);
    }

    //extracts expiration date from 'token'
    public Date extractExpiration (String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    //check if the token is expired
    public Boolean IsTokenExpired (String token) {

        return extractExpiration(token).before(new Date());
    }

    //validates user
    public Boolean ValidateToken(String token, UserDetails userDetails){

        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !IsTokenExpired(token);
    }

    //creates JWT token
    public String createToken (Map<String, Object> claims, String username){

        return Jwts.builder()           //builder design pattern
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    //Function<T, R> is a functional interface used for transforming input (T) to output (R).
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){

        //tried extracting all claims within the token
        final Claims claims = extractAllClaims (token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {

        return Jwts                             //it's an instance of JWT Object
                .parser()                       //new 'JwtParser' instance is returned. This creates a new instance of a JwtParser which is responsible for parsing JWT strings into Jwt objects.
                .verifyWith(getSignKey())    //used to validate that the token is trustworthy and has not been tampered with the help of signature verification key
                .build()                        //This finalizes the JwtParser instance after setting all necessary configurations
                .parseSignedClaims(token)       //parses the JWT string (token) passed to it and extracts the claims defined in the payload of the token. essentially converts the encoded JWT token into a readable object containing all the claims (data) stored in the token.
                .getPayload();                  //returns the claims defined by user as a Claim object
    }

    private SecretKey getSignKey(){

            byte[] keyBytes = Decoders.BASE64.decode(SECRET);
            return Keys.hmacShaKeyFor(keyBytes);
    }

    public String GenerateToken(String username){ //helper function to create JwtToken

        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }
}
