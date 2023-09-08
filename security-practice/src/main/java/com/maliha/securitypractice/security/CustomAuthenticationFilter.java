package com.maliha.securitypractice.security;

import com.maliha.securitypractice.SpringApplicationContext;
import com.maliha.securitypractice.constants.AppConstants;
import com.maliha.securitypractice.model.UserDTO;
import com.maliha.securitypractice.model.UserLoginRequestModel;
import com.maliha.securitypractice.service.UserService;
import com.maliha.securitypractice.utils.JWTUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collections;


//@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            UserLoginRequestModel credentials= new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestModel.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(),credentials.getPasswords()));

        }
        catch (IOException e){
            log.info("Exception occured at attemptAuthentication method: {}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String user = ((User)authResult.getPrincipal()).getUsername();
        String accessToken = JWTUtils.generateToken(user);
        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImplementation");
        UserDTO userDto = userService.getUser(user);
        response.addHeader("userId",userDto.getUserId());
        response.addHeader(AppConstants.HEADER_STRING,AppConstants.TOKEN_PREFIX+accessToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //super.unsuccessfulAuthentication(request, response, failed);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String errorMessage = "Authentication failed: " + failed.getMessage();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(Collections.singletonMap("error", errorMessage));
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}

