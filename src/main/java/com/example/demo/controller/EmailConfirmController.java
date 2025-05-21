package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"},allowCredentials = "true")
public class EmailConfirmController {
	@GetMapping(value = "/email/confirm")
	public String username() {
		return null;
		
	}
	
}
