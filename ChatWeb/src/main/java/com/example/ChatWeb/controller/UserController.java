package com.example.ChatWeb.controller;

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
    public UserController(UserService userService){
        this.userService=userService;
    }
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandle(@RequestHeader("Authorization")String token) throws UserException {
        User user = userService.findUserProfile(token);
        return new ResponseEntity<User>(user,HttpStatus.ACCEPTED);
    }
    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUserHandle(@PathVariable("query")String q) {
        List<User> user = userService.searchUser(q);
        return new ResponseEntity<List<User>>(user,HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandle(@RequestBody UpdateUserRequest req, @RequestHeader("Authorization")String token) throws UserException {
        User user = userService.findUserProfile(token);
        userService.updateUser(user.getId(),req);
        ApiResponse res = new ApiResponse("User Update successfully", true);
        return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
    }
}
