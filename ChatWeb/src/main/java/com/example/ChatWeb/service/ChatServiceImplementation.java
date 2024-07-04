package com.example.ChatWeb.service;

import com.example.ChatWeb.exception.ChatException;
import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.Chat;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.repository.ChatRepository;
import com.example.ChatWeb.request.GroupChatRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImplementation implements ChatService{
    private ChatRepository chatRepository;
    private UserService userService;
    public ChatServiceImplementation(ChatRepository chatRepository,UserService userService){
        this.chatRepository=chatRepository;
        this.userService=userService;
    }
    @Override
    public Chat createChat(User reqUser, Integer userId2) throws UserException {
        User user = userService.findUserById(userId2);
        Chat isChatExist = chatRepository.findSingleChatByUsersId(user, reqUser);
        if (isChatExist!=null){
            return isChatExist;
        }
        Chat chat = new Chat();
        chat.setCreateBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setGroup(false);
        return chat;
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()){
            return chat.get();
        }
        throw new ChatException("Chat not found with id: " +chatId);
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
        User user = userService.findUserById(userId);
        List<Chat> chats = chatRepository.findChatByUserid(user.getId());
        return chats;
    }

    @Override
    public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChat_image(req.getChat_image());
        group.setChat_name(req.getChat_name());
        group.setCreateBy(reqUser);
        group.getAdmins().add(reqUser);
        for(Integer userId:req.getUserIds()){
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }
        return group;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {
        Optional<Chat> otp=chatRepository.findById(chatId);
        User user=userService.findUserById(userId);
        if(otp.isPresent()){
            Chat chat = otp.get();
            if (chat.getAdmins().contains(reqUser)) {
                chat.getUsers().add(user);
                return chatRepository.save(chat);
            }
            else {
                throw new UserException("You not admin");
            }
        }
        throw new ChatException("Chat not found wiht id: "+chatId);
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException {
        Optional<Chat> otp=chatRepository.findById(chatId);
        if (otp.isPresent()){
            Chat chat = otp.get();
            if (chat.getAdmins().contains(reqUser)) {
                chat.setChat_name(groupName);
                return chatRepository.save(chat);
            }
            throw new UserException("You not admin");
        }
        throw new ChatException("Chat not found wiht id: "+chatId);
    }

    @Override
    public Chat removeFormGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
        Optional<Chat> otp=chatRepository.findById(chatId);
        User user=userService.findUserById(userId);
        if(otp.isPresent()){
            Chat chat = otp.get();
            if (chat.getAdmins().contains(reqUser)) {
                chat.getUsers().remove(user);
                return chatRepository.save(chat);
            } else if (chat.getUsers().contains(reqUser)) {
                chat.getUsers().remove(user);
                return chatRepository.save(chat);
            }
            throw new UserException("You cant remove another user");
        }
        throw new ChatException("Chat not found wiht id: "+chatId);
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException {
        Optional<Chat> otp = chatRepository.findById(chatId);
        if (otp.isPresent()){
            Chat chat = otp.get();
            chatRepository.deleteById(chat.getId());
        }
    }
}
