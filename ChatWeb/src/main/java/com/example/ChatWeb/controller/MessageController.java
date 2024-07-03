package com.example.ChatWeb.controller;

import com.example.ChatWeb.exception.ChatException;
import com.example.ChatWeb.exception.MessageException;
import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.Chat;
import com.example.ChatWeb.model.Message;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.request.SendmessageReques;
import com.example.ChatWeb.request.SingleChatRequest;
import com.example.ChatWeb.response.ApiResponse;
import com.example.ChatWeb.service.MessageService;
import com.example.ChatWeb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private MessageService messageService;
    private UserService userService;
    public MessageController (MessageService messageService,UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }
    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendmessageReques req, @RequestHeader("Authorization")String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile (jwt);
        req.setUserId (user.getId ()) ;
        Message message=messageService.sendMessage(req);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatsHandler(@PathVariable Integer chatId,@RequestHeader("Authorization")String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile (jwt);
        List<Message> messages=messageService.getChatsMessages(chatId,user);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable Integer messageId,@RequestHeader("Authorization")String jwt) throws UserException, ChatException, MessageException {
        User user = userService.findUserProfile (jwt);
        messageService.deleteMessage(messageId,user);
        ApiResponse res = new ApiResponse ("message is deleted successfully", true);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
}