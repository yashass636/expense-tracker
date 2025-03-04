package com.SecApp.SpringSec.services;

import com.SecApp.SpringSec.entities.UserInfo;
import com.SecApp.SpringSec.eventProducer.UserInfoEvent;
import com.SecApp.SpringSec.eventProducer.UserInfoProducer;
import com.SecApp.SpringSec.models.UserInfoDto;
import com.SecApp.SpringSec.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

/*
*
* - The UserDetailsService interface plays a crucial role in Spring Security, as it's responsible for loading user-specific data during the
    authentication process. Here's a breakdown of its key roles:

* - Custom User Authentication: The main role of the UserDetailsService interface is to provide a way to fetch user information from a database
    or any other source for custom authentication. When a user attempts to log in, Spring Security calls the loadUserByUsername(String username)
    method to retrieve user details.

* - Security Context: The UserDetailsService interface helps populate the SecurityContext with the authenticated user's details. This is vital for
    ensuring that the user-specific information is available throughout the security context, enabling fine-grained access control.

* - Integration with Other Services: By implementing UserDetailsService, developers can integrate Spring Security with various data sources like
    databases, LDAP servers, or even web services, providing flexibility in how user data is fetched and handled.

* - UserDetails Object: The UserDetails object returned by loadUserByUsername contains essential user information, such as username, password,
    and authorities (roles/permissions). This information is used to create an Authentication object, which is then used by Spring Security to verify the user's
    credentials and determine access rights.

*
* */

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserInfoProducer userInfoProducer;

    /*
    * This method is used to load user-specific data when a user tries to authenticate. The UserDetails object returned
    * by this method contains the necessary information to build an Authentication object.
    * */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //fetching user info from DB.
        UserInfo userInfo = userRepository.findByUsername(username);
        if(userInfo == null){

            throw new UsernameNotFoundException("could not find user in DB...");
        }

        return new CustomUserDetails(userInfo);
    }

    public UserInfo checkIfUserAlreadyExist (UserInfoDto userInfoDto){

        return  userRepository.findByUsername(userInfoDto.getUsername());
    }

    //For SignUp
    public boolean signUp (UserInfoDto userInfoDto){

        //TODO: validate e-mail and password

        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));

        if (Objects.nonNull(checkIfUserAlreadyExist(userInfoDto))){
            return false;
        }

        //generate random unique id for new user
        String userId = UUID.randomUUID().toString();

        UserInfo userInfo = new UserInfo(userId, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>());
        //storing the new user data into the db
        userRepository.save(userInfo);

        userInfoDto.setUserId(userId);
        //push to event-queue (kafka)
        userInfoProducer.sendEventToKafka(userInfoEventToPublish(userInfoDto,userId));

        return true;
    }

    private UserInfoEvent userInfoEventToPublish (UserInfoDto userInfoDto, String userId){

        return UserInfoEvent.builder()
                .userId(userId)
                .firstName(userInfoDto.getFirstName())
                .lastName(userInfoDto.getLastName())
                .email(userInfoDto.getEmail())
                .phoneNumber(userInfoDto.getPhoneNumber())
                .build();
    }
}
