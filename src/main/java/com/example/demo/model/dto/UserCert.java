package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
@AllArgsConstructor
@Getter
@ToString
public class UserCert {
	private Long userId;
	private String userName;
	private boolean emailcheck;
	private String role;
}
