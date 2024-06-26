package com.nithish.blog.controllers;

import com.nithish.blog.entities.User;
import com.nithish.blog.exceptions.ApiException;
import com.nithish.blog.exceptions.ResourceNotFoundException;
import com.nithish.blog.payloads.JwtAuthRequest;
import com.nithish.blog.payloads.JwtAuthResponse;
import com.nithish.blog.payloads.UserDto;
import com.nithish.blog.repositories.UserRepo;
import com.nithish.blog.security.JWTTokenHelper;
import com.nithish.blog.services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(
            @RequestBody JwtAuthRequest request) throws Exception {
            String u = request.getUsername();
            if(!u.contains("@")) throw new ApiException("please enter username as your email id.");
            this.authenticate(request.getUsername(),request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtTokenHelper.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);

        try {
            this.authenticationManager.authenticate(authenticationToken);
        }catch (BadCredentialsException e){
            System.out.println("InValid Details");
            throw new ApiException("Invalid username or password !!");
        }

    }
//    //register new user api

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto){
        UserDto registerNewUser = this.userService.registerNewUser(userDto);

        return new ResponseEntity<>(registerNewUser,HttpStatus.CREATED);
    }

    // get loggedin user data
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper mapper;
    @GetMapping("/current-user/")
    public ResponseEntity<UserDto> getUser(Principal principal) {
        try{
            User user = this.userRepo.findByEmail(principal.getName()).get();
            return new ResponseEntity<UserDto>(this.mapper.map(user, UserDto.class), HttpStatus.OK);
        } catch (Exception e){
            throw new ApiException("No user logged in. please login!!");
        }


    }

}
