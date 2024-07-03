package com.example.ChatWeb.service;

import com.example.ChatWeb.exception.ChatException;
import com.example.ChatWeb.exception.MessageException;
import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.Message;
import com.example.ChatWeb.model.Chat;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.repository.MessageRepository;
import com.example.ChatWeb.request.SendmessageReques;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class MessageServicelmplementation implements MessageService{
    private MessageRepository messageRepository;
    private UserService usersService;
    private ChatService chatService;
    public MessageServicelmplementation (MessageRepository messageRepository,UserService usersService,ChatService chatService) {
        this.messageRepository = messageRepository;
        this.usersService = usersService;
        this.chatService = chatService;
    }

    @Override
    public Message sendMessage(SendmessageReques req) throws UserException, ChatException {
        User user=usersService.findUserById(req.getUserId());
        Chat chat=chatService.findChatById(req.getChatId());
        Message message=new Message ();
        message.setChat(chat) ;
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimestamp (LocalDateTime.now()) ;

        return message;
    }

    @Override
    public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException {
        Chat chat=chatService.findChatById(chatId);
        if (!chat.getUsers () .contains (reqUser)) {
            throw new UserException ("you are not releted to this chat "+chat.getId());
        }
        List<Message> messages=messageRepository.findByChatId(chat.getId());
        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> opt=messageRepository.findById(messageId);
        if (opt.isPresent()){
            return opt.get ();
        }
        throw new MessageException ("message not found with id "+messageId);
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {
        Message message = findMessageById(messageId);
        if(message.getUser().getId().equals(reqUser.getId())) {
            messageRepository.deleteById(messageId);
        }
        throw new UserException ("you cant delete another user's message "+reqUser.getFullname());
    }
}
