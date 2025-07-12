package com.example.demo.dao;

import com.example.demo.model.entity.User;
import org.springframework.context.annotation.Bean;

import java.util.List;

public interface UserDao {
    List<User> findAllUsers();
}
