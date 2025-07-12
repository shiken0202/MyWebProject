package com.example.demo.dao.impl;

import com.example.demo.dao.UserDao;
import com.example.demo.model.entity.User;
import com.example.demo.myBatisMapper.UserMyBatisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    UserMyBatisMapper userMyBatisMapper;

    @Override
    public List<User> findAllUsers() {
        List<User> users=userMyBatisMapper.findAllUsersInfo();
        return users;
    }
}
