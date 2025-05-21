package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.User.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.util.HashUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserMapper userMapper;
	
	@Override
	public void emailConfirmOK(String userName) {
		if(userName==null) {
			return;
		}
		userRepository.emailConfirmOK(userName);
	}

	@Override
	public List<UserDto> findAllUsers() {
		// TODO 自動產生的方法 Stub
		return null;
	}

	@Override
	public UserDto getUserById() {
		// TODO 自動產生的方法 Stub
		return null;
	}

	@Override
	public void addUser(String userName, String userEmail, String password,boolean emailConfirmOK, String role) {
		
		try {
			String salt;
			salt = HashUtil.generateSalt();
			String passwordHash = HashUtil.hashPassword(password, salt);
			User.Role roleEnum=User.Role.valueOf(role.toUpperCase());
			User user = new User();//userName,passwordHash,salt,userEmail,emailConfirmOK,roleEnum
			user.setUserName(userName);
			user.setHashPassword(passwordHash);
			user.setSalt(salt);
			user.setEmail(userEmail);
			user.setEmailConfirmOK(emailConfirmOK);
			user.setRole(roleEnum);
			userRepository.save(user);
		} catch (Exception e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void updateUser(Long userId, String userName, String userEmail, String password, String role) {
		// TODO 自動產生的方法 Stub
		
	}

	@Override
	public void deleteUser(Long userId) {
		// TODO 自動產生的方法 Stub
		
	}



}
