package com.electronicstore.controller;

import com.electronicstore.entity.User;
import com.electronicstore.exception.BadApiRequest;
import com.electronicstore.payload.JwtRequest;
import com.electronicstore.payload.JwtResponse;
import com.electronicstore.payload.UserDto;
import com.electronicstore.security.JwtHelper;
import com.electronicstore.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
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

    @Value("${googleClientId}")
    private String googleclientId;

    @Value("${newPassword}")
    private String newPassword;


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
    @PostMapping("/google")
    public  ResponseEntity<JwtResponse> loginwithGoogle(@RequestBody Map<String,Object> data) throws IOException {
        String idToken = data.get("idToken").toString();
        NetHttpTransport netHttpTransport=new NetHttpTransport();

        JacksonFactory jacksonFactory=new JacksonFactory().getDefaultInstance();

        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleclientId));
        GoogleIdToken googleIdToken=GoogleIdToken.parse(verifier.getJsonFactory(),idToken);
       GoogleIdToken.Payload payload=googleIdToken.getPayload();
       log.info("Payload:{}",payload);

        String email = payload.getEmail();
        User user=null;
     user= userService.findUserByEmailOptional(email).orElseThrow(null);
     if(user==null){
        user= this.saveUser(email,data.get("name").toString(),data.get("phototUrl").toString());

     }
        ResponseEntity<JwtResponse> jwtResponseResponseEntity = this.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());
     return jwtResponseResponseEntity;
    }

    private User saveUser(String email, String name, String phototUrl) {
        UserDto newuser = UserDto.builder()
                .name(name)
                .email(email)
                .password(newPassword)
                .imageName(phototUrl)
                .roles(new HashSet<>())
                .build();

        UserDto user = this.userService.createUser(newuser);
        return this.modelMapper.map(user,User.class);
    }
}
