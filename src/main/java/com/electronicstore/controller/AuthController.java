package com.electronicstore.controller;

import com.electronicstore.exception.BadApiRequest;
import com.electronicstore.payload.JwtRequest;
import com.electronicstore.payload.JwtResponse;
import com.electronicstore.payload.UserDto;
import com.electronicstore.security.JwtHelper;
import com.electronicstore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping("/auth/current")
    public ResponseEntity<UserDetails> getCurrentUser(Principal principal){
        String name = principal.getName();
        return new ResponseEntity<>(userDetailsService.loadUserByUsername(name), HttpStatus.OK);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        this.doAuthenticate(request.getEmail(),request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);

        UserDto userDto=modelMapper.map(userDetails,UserDto.class);

        JwtResponse response = JwtResponse.builder().jwtToken(token)
                .user(userDto).build();
        return  new ResponseEntity<>(response,HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(email,password);
       try{
           authenticationManager.authenticate(authentication);
       }catch (BadCredentialsException e){
    throw new BadApiRequest("Invaid username pasword");
       }
    }
}
