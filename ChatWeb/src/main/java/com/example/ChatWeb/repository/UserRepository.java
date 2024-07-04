package com.example.ChatWeb.repository;

import com.example.ChatWeb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.full_name LIKE %:query% or u.email LIKE %:query%")
    public List<User> searchUsers (@Param("query")String query);
}
