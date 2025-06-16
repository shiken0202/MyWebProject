package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
@AllArgsConstructor
@Getter
@Data
@ToString
public class UserCert {
	private Long userId;
	private String userName;
	private boolean emailcheck;
	private String role;
	private boolean isbanned;
}
