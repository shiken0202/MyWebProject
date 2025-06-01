package com.example.demo.service;

import java.util.List;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.dto.UserListDto;

public interface UserService {

	public	void emailConfirmOK(String userName);
	public 	List<UserListDto> findAllUsers();
	public	UserDto getUserById(Long userId);
	public  UserDto getUserByUserName(String userName);
	public 	boolean existsByUserName(String userName);
	public	void addUser(String userName,String userEmail,String password,String role);
	public	void updateUser(Long userId,String userName,String userEmail,String password,String role);
	public	void deleteUser(Long userId);
	
}
