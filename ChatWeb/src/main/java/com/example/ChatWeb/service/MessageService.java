package com.example.ChatWeb.service;

import com.example.ChatWeb.exception.ChatException;
import com.example.ChatWeb.exception.MessageException;
import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.Message;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.request.SenMessageRequest;

import java.util.List;

public interface MessageService {
    public Message sendMessage(SenMessageRequest req) throws UserException, ChatException;
    public List<Message> getChatMessage(Integer chatId, User reqUser) throws ChatException, UserException;
    public Message findMessageById(Integer messageId) throws MessageException;
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException;
}
