package com.ng.runz.controller;

import com.ng.runz.dto.UserDto;
import com.ng.runz.exception.ResourceAlreadyExistException;
import com.ng.runz.model.Users;
import com.ng.runz.request.LoginRequest;
import com.ng.runz.request.RegisterUserRequest;
import com.ng.runz.response.ApiResponse;
import com.ng.runz.service.JwtUtilService;
import com.ng.runz.service.impl.UsersServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

import java.util.AbstractMap;
import java.util.HashMap;

@RestController
public class AuthController {

    private final JwtUtilService jwtUtilService;
    private final AuthenticationManager authenticationManager;
    private final UsersServiceImpl usersServiceImpl;

    public AuthController(JwtUtilService jwtUtilService, AuthenticationManager authenticationManager, UsersServiceImpl usersServiceImpl){
        this.jwtUtilService = jwtUtilService;
        this.authenticationManager=authenticationManager;
        this.usersServiceImpl = usersServiceImpl;
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password()));
            if(authentication.isAuthenticated()){
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities()));
                Users user = usersServiceImpl.getUserByUsername(userDetails.getUsername());
                String jwtToken = jwtUtilService.generateJwtToken(user);
                return ResponseEntity.ok().body(new ApiResponse("success",jwtToken));
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse("login failed",null));
            }

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PostMapping("register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterUserRequest request){

        try {
            UserDto userDto = new UserDto(request.username(),request.email(),request.password(),request.roles());
            usersServiceImpl.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success",null));
        }catch (ResourceAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse("failed",e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse("failed",null));
        }

    }

}
