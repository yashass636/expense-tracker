package com.SecApp.SpringSec.auth;

import com.SecApp.SpringSec.repository.UserRepository;
import com.SecApp.SpringSec.services.UserDetailServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
*
* - @EnableMethodSecurity : used to enable method-level security in a Spring application.
* - It allows you to apply security constraints at the method level using annotations like @PreAuthorize, @PostAuthorize, @Secured, and @RolesAllowed
* - When you add @EnableMethodSecurity to a configuration class, Spring Security will scan for security annotations on methods and enforce the specified security constraints.
*
* */

@Configuration
@EnableMethodSecurity //used to enable method-level security in a Spring application. It allows you to apply security constraints at the method level using annotations like @PreAuthorize, @PostAuthorize, @Secured, and @RolesAllowed
@Data
public class SecurityConfig {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserDetailServiceImpl userDetailServiceImpl;

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder){

        return new UserDetailServiceImpl();
    }

    //TODO: Need to study about the below bean
    /*
    *
    * "UsernamePasswordAuthenticationFilter" is a built-in Spring Security filter that processes username and password authentication.
    * It intercepts login requests (usually at /login) and attempts to authenticate the user.
    *
    * */
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception{

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(CorsConfigurer::disable)
                .authorizeHttpRequests( auth -> auth

                        .requestMatchers("/auth/v1/login", "/auth/v1/refreshToken", "/auth/v1/signup").permitAll() // these link won't be authenticated
                        .anyRequest().authenticated() //except the above list. remaining links will be authenticated
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) //TODO: Study about this.  Now JwtAuthFilter will intercept specific request automatically.
                .authenticationProvider(authenticationProvider())                           //TODO: Study about this
                .build();
    }

    //TODO : **Study about "DaoAuthenticationProvider"**
    @Bean
    public AuthenticationProvider  authenticationProvider () {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return daoAuthenticationProvider;
    }


    //TODO : **Study about "AuthenticationManager" and "AuthenticationConfiguration"**
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{

        return config.getAuthenticationManager();
    }
}
