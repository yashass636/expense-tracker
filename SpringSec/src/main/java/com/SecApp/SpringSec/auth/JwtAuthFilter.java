package com.SecApp.SpringSec.auth;

import com.SecApp.SpringSec.services.JwtService;
import com.SecApp.SpringSec.services.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
*
* Step-by-Step JWT Authentication Flow :
* -------------------------------------------
*        1. User logs in with username and password.
*        2. Spring Security authenticates the user and generates a JWT token.
*        3. The JWT is sent back to the client and included in subsequent requests in the Authorization header.
*        4. A JWT Filter extracts the token, validates it, and loads the user's authentication into the SecurityContextHolder.
*
* */

/*
* - OncePerRequestFilter is an abstract class in Spring Boot (part of the org.springframework.web.filter package) that ensures
*   a filter is executed only once per request, even in cases of internal forwarding or multiple dispatches.
*
*   Step 1: The user submits their login credentials through a login form.

*   Step 2: The JwtAuthFilter (which extends OncePerRequestFilter) intercepts the request.

*   Step 3: The filter validates the user's credentials (using token).

*   Step 4: If the user is authenticated, the filter allows the request to proceed by calling filterChain.doFilter(request, response).
            Otherwise, it sends an unauthorized error response.
*
* */

/*
*
* - The JwtAuthFilter does not automatically intercept requests unless it is explicitly registered in the Spring Security filter chain.
* - (see the code in "securityFilterChain" method in "SecurityConfig" class)
*
* */

@Component
@AllArgsConstructor
@Data
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailServiceImpl userDetailServiceImpl;

    /*
    *
    * - The doFilterInternal method of OncePerRequestFilter ensures that the authentication check is performed only once per request.
    * - Even if the request is forwarded to another servlet or resource, the filter will not re-execute the authentication logic.
    * - This helps in avoiding redundant processing and ensures efficient handling of requests.
    *
    * */
    /*
    * "filterChain" : is an instance of FilterChain, which represents a chain of filters that process the request before it reaches the controller
    * */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader   =  request.getHeader("Authorization");
        String token        = null;
        String username     = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")){

            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);

        }

        // "SecurityContextHolder.getContext().getAuthentication()" is used to retrieve the authentication details of the currently logged-in user.
        /*
        * SecurityContextHolder:
        *    - This is a Spring Security class that holds the security context for the current thread.
        *    - It stores authentication and security-related details.
        *    - The SecurityContext is stored per thread.
        *
        * When is SecurityContextHolder Populated?
        *    - After successful login: Spring Security sets the authentication object.
        *    - After JWT token verification (if using JWT authentication).
        *    - In custom authentication mechanisms.
        *
        * getContext():
        *    - Retrieves the SecurityContext, which contains security-related details.
        *
        * getAuthentication():
        *    - Returns an "Authentication object" that holds details about the currently authenticated user.
        *    - If the user is not authenticated, it returns null.
        *
        * What is inside the Authentication object?
        * The returned Authentication object contains:
        *   - Principal: The currently authenticated user (can be a UserDetails object or a username).
        *   - Credentials: Usually the password (maybe null after authentication for security reasons).
        *   - Authorities: The roles/permissions assigned to the user.
        *   - Authenticated Flag: Indicates whether the user is authenticated.
        *
        * */
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);

            if (jwtService.ValidateToken(token, userDetails)){

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        /*
        * What Happens When You Call doFilter(request, response)
        * - It hands over the request and response to the next filter in the chain.
        * - If it's the last filter, the request reaches the controller.
        * - If not called, the request will be blocked and won't proceed further. The request is stuck indefinitely.
        * */
        filterChain.doFilter(request, response);
    }
}
