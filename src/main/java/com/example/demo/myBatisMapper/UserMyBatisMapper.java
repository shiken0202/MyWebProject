package com.example.demo.myBatisMapper;


import com.example.demo.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMyBatisMapper {
    @Select("SELECT id,user_name,email,email_confirmok,role,isbanned FROM users")
    public List<User> findAllUsersInfo();
}
