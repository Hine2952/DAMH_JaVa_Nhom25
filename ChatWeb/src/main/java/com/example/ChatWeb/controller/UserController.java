package com.example.ChatWeb.controller;

import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.request.UpdateUserRequest;
import com.example.ChatWeb.request.UserDto;
import com.example.ChatWeb.response.ApiResponse;
import com.example.ChatWeb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
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
    @GetMapping ("/search")
    public ResponseEntity<HashSet<UserDto>> searchUsersByName(@RequestParam("name") String name) {

        System.out.println ("search user ------- ");

        List<User> users=userService.searchUser(name);

        HashSet<User> set=new HashSet<> (users);

        HashSet<UserDto> userDtos=UserDtoMapper.toUserDtos(set);

        System.out.println("search result ------ "+userDtos);

        return new ResponseEntity<>(userDtos,HttpStatus.ACCEPTED);
    }
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandle(@RequestBody UpdateUserRequest req, @RequestHeader("Authorization")String token) throws UserException {
        User user = userService.findUserProfile(token);
        userService.updateUser(user.getId(),req);
        ApiResponse res = new ApiResponse("User Update successfully", true);
        return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
    }
    public class UserDtoMapper {
        public static HashSet<UserDto> toUserDtos(HashSet<User> users) {
            HashSet<UserDto> userDtos = new HashSet<>();
            for (User user : users) {
                UserDto dto = new UserDto();
                dto.setId(user.getId());
                dto.setFull_name(user.getFull_name());
                dto.setEmail(user.getEmail());
                dto.setProfile_picture(user.getProfile_picture());
                dto.setPassword(user.getPassword());
                // Thiết lập các thuộc tính khác của UserDto
                userDtos.add(dto);
            }
            return userDtos;
        }
    }
}
