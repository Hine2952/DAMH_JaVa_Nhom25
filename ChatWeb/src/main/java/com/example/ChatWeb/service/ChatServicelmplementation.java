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
public class ChatServicelmplementation implements ChatService{
    private ChatRepository chatRepository;
    private UserService userService;
    public ChatServicelmplementation (ChatRepository chatRepository,UserService userService) {
        this.chatRepository=chatRepository;
        this.userService=userService;
    }
    @Override
    public Chat createChat(User reqUser, Integer userId2) throws UserException {
        User user=userService.findUserById(userId2) ;
        Chat isChatExist=chatRepository.findSingleChatByUserIds (user, reqUser);
        if(isChatExist==null) {
            return isChatExist;
        }
        Chat chat=new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setGroup(false);
        return chat;
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat=chatRepository.findById(chatId);
        if (chat.isPresent()){
            return chat.get();
        }
        throw new ChatException ("Chat not found with id "+chatId);
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userid) throws UserException {
        User user =userService.findUserById(userid);
        List<Chat> chats = chatRepository.findChatByUserid(user.getId());
        return chats;
    }

    @Override
    public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException {
        Chat group=new Chat ();
        group.setGroup (true) ;
        group.setChatimage (req.getChatimage());
        group.setChatname (req.getChatname());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);
        for (Integer userId:req.getUserIds()) {
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }
        return group;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {
        Optional<Chat> opt=chatRepository.findById(chatId);
        User user=userService.findUserById(userId);
        if (opt.isPresent ()) {
            Chat chat = opt.get();
            if (chat.getAdmins().contains(reqUser)) {
                chat.getUsers().add(user);
                return chatRepository.save(chat);
            }
            else
                throw new UserException("You are not admin");
        }
        throw new ChatException("chat not found with id "+chatId);
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException {
        Optional<Chat> opt=chatRepository.findById(chatId);
        if (opt.isPresent()) {
            Chat chat=opt.get ();
            if (chat.getUsers () .contains (reqUser)) {
                chat.setChatname(groupName);
                return chatRepository.save(chat);
            }
            throw new UserException ("you are not member of this group");
        }
        throw new ChatException("chat not found with id "+chatId);
    }

    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
        Optional<Chat> opt=chatRepository.findById(chatId);
        User user=userService.findUserById(userId);
        if (opt.isPresent ()) {
            Chat chat = opt.get();
            if (chat.getAdmins().contains(reqUser)) {
                chat.getUsers().remove(user);
                return chatRepository.save(chat);
            }
            else if (chat.getUsers().contains(reqUser)) {
                if (user.getId().equals(reqUser.getId())){
                    chat.getUsers().remove(user);
                    return chatRepository.save(chat);

                }
            }
            else
                throw new UserException("You can't remove another user");
        }
        throw new ChatException("chat not found with id "+chatId);
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException {
        Optional<Chat> opt = chatRepository.findById(chatId);
        if (opt.isPresent()) {
            Chat chat = opt.get();
            chatRepository.deleteById(chat.getId());
        }
    }
}
