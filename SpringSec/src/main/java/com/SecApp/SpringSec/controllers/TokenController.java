package com.SecApp.SpringSec.controllers;

import com.SecApp.SpringSec.entities.RefreshToken;
import com.SecApp.SpringSec.request.AuthRequestDTO;
import com.SecApp.SpringSec.request.RefreshTokenRequest;
import com.SecApp.SpringSec.response.JwtResponseDTO;
import com.SecApp.SpringSec.services.JwtService;
import com.SecApp.SpringSec.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    //this is called when refresh token is expired
    @PostMapping("auth/v1/login")
    public ResponseEntity AuthenticationAndGetToken (@RequestBody AuthRequestDTO authRequestDTO){

        Authentication authentication = authenticationManager
                                        .authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));

        // The below "isAuthenticated ()" will do the job of checking if proper username and password is entered by user.
        // The configuration are done in "SecurityConfig" class.
        if(authentication.isAuthenticated()){

            RefreshToken refreshToken = refreshTokenService.createRefreshToken (authRequestDTO.getUsername());
            return new ResponseEntity <> (JwtResponseDTO.builder()
                    .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                    .token(refreshToken.getToken())
                    .build(), HttpStatus.OK);

        } else {

            return new ResponseEntity<> ("Pls try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //this is called when Refresh token is fine but Jwt token is expired
    @PostMapping("auth/v1/refreshToken")
    public JwtResponseDTO refreshToken (@RequestBody RefreshTokenRequest refreshTokenRequest){

        return  refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());

                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequest.getToken()).build();
                }).orElseThrow(() -> new RuntimeException("refresh token not in DB"));

    }

}
