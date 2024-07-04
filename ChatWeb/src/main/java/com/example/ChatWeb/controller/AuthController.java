package com.example.ChatWeb.controller;

import com.example.ChatWeb.config.TokenProvider;
import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.repository.UserRepository;
import com.example.ChatWeb.request.LoginRequest;
import com.example.ChatWeb.response.AuthResponse;
import com.example.ChatWeb.service.CustomUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.prefs.BackingStoreException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenProvider tokenProvider;
    private CustomUserService customUserService;
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomUserService customUserService, TokenProvider tokenProvider){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.customUserService=customUserService;
        this.tokenProvider=tokenProvider;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/signup",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> createUserHandle(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String full_name = user.getFull_name();
        String password = user.getPassword();
        User isUser = userRepository.findByEmail(email);
        if (isUser!=null){
            throw new UserException("Email is used wiht another account: " +email);
        }
        User createUser = new User();
        createUser.setEmail(email);
        createUser.setFull_name(full_name);
        createUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(createUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse res = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(res, HttpStatus.ACCEPTED);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> loginHandle(@RequestBody LoginRequest req){
        String email = req.getEmail();
        String password = req.getPassword();
        Authentication authentication = authenticate(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse res = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(res,HttpStatus.ACCEPTED);
    }

    public Authentication authenticate(String Username, String password){
        UserDetails userDetails = customUserService.loadUserByUsername(Username);
        if (userDetails==null){
            throw new BadCredentialsException("invalid username");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("invalid password or username");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

