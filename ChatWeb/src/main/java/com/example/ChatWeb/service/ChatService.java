package com.example.ChatWeb.service;

import com.example.ChatWeb.exception.ChatException;
import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.Chat;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.request.GroupChatRequest;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface ChatService {
    public Chat createChat(User reqUser, Integer userId2) throws UserException;
    public Chat findChatById(Integer chatId) throws ChatException;
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException;
    public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException;
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException;
    public Chat renameGroup(Integer chatId, String groupName,User reqUser) throws ChatException,UserException;
    public Chat removeFormGroup(Integer chatId, Integer userId,User reqUser) throws UserException,ChatException;
    public void deleteChat(Integer chatId, Integer userId) throws ChatException,UserException;

}
