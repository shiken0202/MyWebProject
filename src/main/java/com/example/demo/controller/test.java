package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@RestController
public class test {

	@GetMapping("/test")
	public String hello() {
		return "Hello world";
	}
}
