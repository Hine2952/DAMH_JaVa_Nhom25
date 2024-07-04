package com.example.ChatWeb.service;

import com.example.ChatWeb.config.TokenProvider;
import com.example.ChatWeb.exception.UserException;
import com.example.ChatWeb.model.User;
import com.example.ChatWeb.repository.UserRepository;
import com.example.ChatWeb.request.UpdateUserRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServicelmplementation implements UserService{
    private UserRepository userRepository;
    private TokenProvider tokenProvider;
    public UserServicelmplementation(UserRepository userRepository, TokenProvider tokenProvider){
        this.userRepository=userRepository;
        this.tokenProvider=tokenProvider;
    }
    @Override
    public User findUserById(Integer id) throws UserException {
        Optional<User> otp = userRepository.findById(id);
        if(otp.isPresent()){
            return otp.get();
        }
        throw new UserException("User not found with id: "+id);
    }

    @Override
    public User findUserProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);
        if(email==null){
            throw new BadCredentialsException("received invalid token ...");
        }
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new UserException("User not found with email: "+email);
        }
        return user;
    }

    @Override
    public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
        User user = findUserById(userId);
        if (req.getFull_name()!= null){
            user.setFull_name(req.getFull_name());
        }
        if (req.getProfile_picture()!= null){
            user.setProfile_picture(req.getProfile_picture());
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {
        List<User> users = userRepository.searchUsers(query);
        return users;
    }
}
