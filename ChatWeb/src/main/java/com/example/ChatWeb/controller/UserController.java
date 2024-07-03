package com.example.ChatWeb.controller;

import com.example.ChatWeb.config.TokenProvider;
import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.request.UpdateUserRequest;
import com.example.ChatWeb.response.ApiResponse;
import com.example.ChatWeb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    private TokenProvider tokenProvider;

    public UserController(UserService userService, TokenProvider tokenProvider) {
        this.userService=userService;
        this.tokenProvider=tokenProvider;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler (@RequestHeader("Authorization") String token) throws UserException {
            User user = userService.findUserProfile(token);
            return new ResponseEntity<User>(user, HttpStatus.ACCEPTED) ;
    }

    @GetMapping ("/{query}")
    public ResponseEntity<List<User>> searchUserHandler(@PathVariable("query") String q){
        List<User> users=userService.searchUser(q);
        return new ResponseEntity<List<User>> (users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse>updateUeserHandler(@RequestBody UpdateUserRequest req, @RequestHeader ("Authorization") String token) throws UserException {
        User user = userService.findUserProfile(token);
        userService.updateUser(user.getId(),req);
        ApiResponse res = new ApiResponse("user update successfully", true);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
    }
}
