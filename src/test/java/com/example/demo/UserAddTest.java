package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.example.demo.service.UserService;

@SpringBootTest
public class UserAddTest {
	@Autowired
	UserService userService;
	
	@Test
	public void UserTest() {
	userService.addUser("admin","hsuan0202.tw@gmail.com", "1234", "admin");
	}
}
