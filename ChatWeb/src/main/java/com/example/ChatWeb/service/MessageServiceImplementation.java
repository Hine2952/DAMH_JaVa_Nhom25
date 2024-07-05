package com.example.ChatWeb.service;

import com.example.ChatWeb.exception.ChatException;
import com.example.ChatWeb.exception.MessageException;
import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.Chat;
import com.example.ChatWeb.model.Message;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.repository.MessageRepository;
import com.example.ChatWeb.request.SenMessageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class MessageServiceImplementation implements MessageService{
    private UserService userService;
    private ChatService chatService;
    private MessageRepository messageRepository;
    public MessageServiceImplementation(UserService userService, ChatService chatService, MessageRepository messageRepository){
        this.chatService=chatService;
        this.userService=userService;
        this.messageRepository=messageRepository;
    }
    @Override
    public Message sendMessage(SenMessageRequest req) throws UserException, ChatException {
        User user= userService.findUserById(req.getUserId());
        Chat chat= chatService.findChatById(req.getChatId());
        Message message= new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        return message;
    }

    @Override
    public List<Message> getChatMessage(Integer chatId, User reqUser) throws ChatException, UserException {
        Chat chat = chatService.findChatById(chatId);
        if (!chat.getUsers().contains(reqUser)){
            throw new UserException("You are not related to this chat " + chat.getId());
        }
        List<Message> messages=messageRepository.findByChatId(chat.getId());
        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> otp = messageRepository.findById(messageId);
        if (otp.isPresent()){
            return otp.get();
        }
        throw new MessageException("Message not found with id: "+messageId);
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {
        Message message = findMessageById(messageId);
        if (message.getUser().getId().equals(reqUser.getId())){
            messageRepository.deleteById(messageId);
        }
        throw new UserException("You cant delete anther user's message "+ reqUser.getFull_name());
    }
}
