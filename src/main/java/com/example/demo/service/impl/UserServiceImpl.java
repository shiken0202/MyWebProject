package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
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
	public UserDto getUserById(Long userId) {
		// TODO 自動產生的方法 Stub
		return null;
	}
	@Override
	public UserDto getUserByUserName(String userName) {
		Optional<User>optUser=userRepository.findByUserName(userName);
		if(optUser.isEmpty()) {
			throw new UserNotFoundException("找不到使用者: "+userName);
		}
		User user=optUser.get();
		UserDto userDto=userMapper.toDto(user);
		return userDto;
	}

	@Override
	public void addUser(String userName, String userEmail, String password, String role) {
		
		try {
			String salt;
			salt = HashUtil.generateSalt();
			String passwordHash = HashUtil.hashPassword(password, salt);
			User.Role roleEnum=User.Role.valueOf(role.toUpperCase());
			User user = new User();
			user.setUserName(userName);
			user.setHashPassword(passwordHash);
			user.setSalt(salt);
			user.setEmail(userEmail);
			user.setRole(roleEnum);
			userRepository.save(user);
		} catch (Exception e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void updateUser(Long userId, String userName, String userEmail, String password, String role) {
		Optional<User> optUser=userRepository.findById(userId);
		if(optUser.isEmpty()) {
			throw new UserNotFoundException("無此使用者，無法修改");
		}
		try {
			String salt;
			salt = HashUtil.generateSalt();
			String passwordHash = HashUtil.hashPassword(password, salt);
		User user=optUser.get();
		user.setUserName(userName);
		user.setEmail(userEmail);
		user.setHashPassword(passwordHash);
		User.Role roleEnum=User.Role.valueOf(role.toUpperCase());
		user.setRole(roleEnum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteUser(Long userId) {
		Optional<User> optUser=userRepository.findById(userId);
		if(optUser.isEmpty()) {
			throw new UserNotFoundException("無此使用者，刪除失敗");
		}
		User user=optUser.get();
		userRepository.delete(user);
		
	}

	@Override
	public boolean existsByUserName(String userName) {
		
		return userRepository.existsByUserName(userName);
	}



}
