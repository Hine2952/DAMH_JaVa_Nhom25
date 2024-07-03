package com.example.ChatWeb.controller;

import com.example.ChatWeb.exception.ChatException;
import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.Chat;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.request.GroupChatRequest;
import com.example.ChatWeb.request.SingleChatRequest;
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
    private UserService userServices;
    public ChatController(ChatService chatService, UserService userService) {
        this.chatService=chatService;
        this.userServices=userService;
    }
    @PostMapping("/single")
    public ResponseEntity<Chat>createChatHandler(@RequestBody SingleChatRequest singleChatRequest, @RequestHeader("Authorization")String jwt) throws UserException {
        User reqUser=userServices.findUserProfile(jwt);
        Chat chat=chatService.createChat (reqUser, singleChatRequest.getUserld());
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }
    @PostMapping("/group")
    public ResponseEntity<Chat>createGroupChatHandler(@RequestBody GroupChatRequest req, @RequestHeader("Authorization")String jwt) throws UserException {
        User reqUser=userServices.findUserProfile (jwt);
        Chat chat=chatService.createGroup (req, reqUser);
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }
    @GetMapping ("/{chatId}")
    public ResponseEntity<Chat>findChatByIdHandler(@PathVariable Integer chatId,@RequestHeader("Authorization")String jwt) throws ChatException {
        Chat chat=chatService.findChatById(chatId);
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }
    @GetMapping ("/user")
    public ResponseEntity<List<Chat>>findAllCatByUserIdHandler(@RequestHeader("Authorization")String jwt) throws UserException {
        User reqUser=userServices.findUserProfile (jwt);
        List<Chat> chats =chatService.findAllChatByUserId(reqUser.getId());
        return new ResponseEntity<List<Chat>>(chats,HttpStatus.OK);
    }
    @PutMapping ("/{chatId}/add/{userId}")
    public ResponseEntity<Chat>addUserToGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userID,@RequestHeader("Authorization")String jwt) throws UserException, ChatException {
        User reqUser=userServices.findUserProfile(jwt);
        Chat chat =chatService.addUserToGroup(userID,chatId,reqUser);
        return new ResponseEntity<Chat>(chat,HttpStatus.OK);
    }
    @PutMapping ("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat>removeUserToGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userID,@RequestHeader("Authorization")String jwt) throws UserException, ChatException {
        User reqUser=userServices.findUserProfile(jwt);
        Chat chat =chatService.removeFromGroup(userID,chatId,reqUser);
        return new ResponseEntity<>(chat,HttpStatus.OK);
    }
    @DeleteMapping ("/delete/{chatId}")
    public ResponseEntity<ApiResponse>deleteChatHandler(@PathVariable Integer chatId, @RequestHeader("Authorization")String jwt) throws ChatException, UserException {
        User reqUser=userServices.findUserProfile(jwt);
        chatService.deleteChat(chatId, reqUser.getId());
        ApiResponse res = new ApiResponse ("chat is deleted successfully", true);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
}