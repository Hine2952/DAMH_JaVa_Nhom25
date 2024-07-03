package com.example.ChatWeb.service;

import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.request.UpdateUserRequest;

import java.util.List;

public interface UserService {

    public User findUserById (Integer id) throws UserException;
    public User findUserProfile (String jwt) throws UserException;
    public User updateUser (Integer userid, UpdateUserRequest req) throws UserException;
    public List<User> searchUser (String query);

}
