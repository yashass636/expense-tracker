package com.SecApp.SpringSec.services;

import com.SecApp.SpringSec.entities.RefreshToken;
import com.SecApp.SpringSec.entities.UserInfo;
import com.SecApp.SpringSec.repository.RefreshTokenRepository;
import com.SecApp.SpringSec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/*
*  Functions:
*   - creates refresh token
*   - fetches user by username,
*   - saves refresh token
*   - return's refresh token
*   - verify Expiration of refresh token
* */
@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    //used to create refresh token
    public RefreshToken createRefreshToken (String username) {

        UserInfo userInfoExtract = userRepository.findByUsername(username);

        //below is similar to (= new RefreshToken(userInfoExtract,UUID.randomUUID().toString(),Instant.now().plusMillis(600000)))
        RefreshToken refreshToken = RefreshToken.builder()
                                    .userInfo(userInfoExtract)
                                    .token(UUID.randomUUID().toString())
                                    .expiryDate(Instant.now().plusMillis(600000))
                                    .build();

        return refreshTokenRepository.save(refreshToken);
    }

    //verify the if the refresh token is not expired
    public RefreshToken verifyExpiration (RefreshToken token){

        if(token.getExpiryDate().compareTo(Instant.now()) < 0){

            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + "Refresh token is expired. Please login again...");
        }

        return token;
    }

    public Optional<RefreshToken> findByToken (String token) {

        return refreshTokenRepository.findByToken(token);
    }
}
