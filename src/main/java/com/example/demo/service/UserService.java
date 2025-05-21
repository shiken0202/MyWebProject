package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.User.Role;

public interface UserService {

	public	void emailConfirmOK(String userName);
	public 	List<UserDto> findAllUsers();
	public	UserDto getUserById();
	public	void addUser(String userName,String userEmail,String password,boolean emailConfrimOk,String role);
	public	void updateUser(Long userId,String userName,String userEmail,String password,String role);
	public	void deleteUser(Long userId);
}
