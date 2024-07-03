package com.example.ChatWeb.repository;

import com.example.ChatWeb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>{
    public User findByEmail(String email);
    @Query("select u from User u where u.fullname Like %:query% or u.email Like %:query%")
    public List<User>searchUser(@Param("query") String query);
}
