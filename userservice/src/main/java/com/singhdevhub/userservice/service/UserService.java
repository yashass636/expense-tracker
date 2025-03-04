package com.singhdevhub.userService.service;

import com.singhdevhub.userService.entities.UserInfo;
import com.singhdevhub.userService.entities.UserInfoDto;
import com.singhdevhub.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserInfoDto createOrUpdateUser (UserInfoDto userInfoDto){

        Function<UserInfo, UserInfo> updatingUser = user -> {

            return userRepository.save(userInfoDto.transformToUserInfo());
        };

        Supplier<UserInfo> createUser = () -> {
            return userRepository.save(userInfoDto.transformToUserInfo());
        };

        UserInfo userInfo = userRepository.findByUserId(userInfoDto.getUserId())
                .map(updatingUser)
                .orElseGet(createUser);

        return new UserInfoDto(
                        userInfo.getUserId(),
                        userInfo.getFirstName(),
                        userInfo.getLastName(),
                        userInfo.getPhoneNumber(),
                        userInfo.getEmail(),
                        userInfo.getProfilePic()
                    );
    }

    public UserInfoDto getUser (UserInfoDto userInfoDto) throws Exception {

        Optional<UserInfo> userInfoOpt = userRepository.findByUserId(userInfoDto.getUserId());

        if(userInfoOpt.isEmpty()){
            throw new Exception("user not found");
        }

        UserInfo userInfo = userInfoOpt.get();

        return new UserInfoDto(
                userInfo.getUserId(),
                userInfo.getFirstName(),
                userInfo.getLastName(),
                userInfo.getPhoneNumber(),
                userInfo.getEmail(),
                userInfo.getProfilePic()
        );
    }
}
