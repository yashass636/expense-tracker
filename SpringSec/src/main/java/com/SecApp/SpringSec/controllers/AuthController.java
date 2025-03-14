package com.SecApp.SpringSec.controllers;

import com.SecApp.SpringSec.entities.RefreshToken;
import com.SecApp.SpringSec.entities.UserInfo;
import com.SecApp.SpringSec.models.UserInfoDto;
import com.SecApp.SpringSec.response.JwtResponseDTO;
import com.SecApp.SpringSec.services.JwtService;
import com.SecApp.SpringSec.services.RefreshTokenService;
import com.SecApp.SpringSec.services.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailServiceImpl userDetailServiceImpl;

    @PostMapping("auth/v1/signup")
    public ResponseEntity signUp(@RequestBody UserInfoDto userInfoDto){

        try{

            Boolean isSignedUp = userDetailServiceImpl.signUp(userInfoDto); //check if user exist in the DB. If not user details are saved in DB

            if(Boolean.FALSE.equals(isSignedUp)){

                return new ResponseEntity<>("User already exist!", HttpStatus.BAD_REQUEST);
            }

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername()); //creates refresh token
            String       token        = jwtService.GenerateToken(userInfoDto.getUsername());               //creates Jwt token

            return new ResponseEntity<> (JwtResponseDTO.builder().accessToken(token)
                    .token(refreshToken.getToken()).build(), HttpStatus.OK);

        }catch (Exception ex){

            return new ResponseEntity<> ("internal error check", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping () {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()){

            return ResponseEntity.ok("Pong");
        } else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }
}
