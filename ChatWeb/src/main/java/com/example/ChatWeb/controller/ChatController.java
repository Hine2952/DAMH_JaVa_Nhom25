package com.example.ChatWeb.controller;

import com.example.ChatWeb.exception.ChatException;
import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.Chat;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.request.GroupChatRequest;
import com.example.ChatWeb.request.SingleChatRequest;
import com.example.ChatWeb.request.UpdateUserRequest;
import com.example.ChatWeb.response.ApiResponse;
import com.example.ChatWeb.service.ChatService;
import com.example.ChatWeb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private ChatService chatService;
    private UserService userService;
    public ChatController(ChatService chatService,UserService userService){
        this.chatService = chatService;
        this.userService = userService;
    }
    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandle(@RequestBody SingleChatRequest singleChatRequest, @RequestHeader("Authorization")String jwt) throws UserException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createChat(reqUser, singleChatRequest.getUserId());
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }
    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupHandle(@RequestBody GroupChatRequest req, @RequestHeader("Authorization")String jwt) throws UserException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createGroup(req, reqUser);
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }
    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandle(@PathVariable Integer chatId,@RequestHeader("Authorization")String jwt) throws ChatException {
        Chat chat = chatService.findChatById(chatId);
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Chat>> findAllChatByUserId(@RequestHeader("Authorization")String jwt) throws UserException {
        User reqUser = userService.findUserProfile(jwt);
        List<Chat> chats = chatService.findAllChatByUserId(reqUser.getId());
        return new ResponseEntity<List<Chat>>(chats,HttpStatus.OK);
    }
    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandle(@PathVariable Integer chatId, @PathVariable Integer userId, @RequestHeader("Authorization")String jwt) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.addUserToGroup(userId, chatId, reqUser);
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }
    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFromGroupHandle(@PathVariable Integer chatId, @PathVariable Integer userId, @RequestHeader("Authorization")String jwt) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.removeFormGroup(chatId, userId, reqUser);
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }
    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandle(@PathVariable Integer chatId, @PathVariable Integer userId, @RequestHeader("Authorization")String jwt) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwt);
        chatService.deleteChat(chatId,reqUser.getId());
        ApiResponse res = new ApiResponse("Chat is delete",false);
        return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
    }
}
