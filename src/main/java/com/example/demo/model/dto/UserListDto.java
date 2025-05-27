package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDto {
	 private Long id;
	 private String userName;
	 private String email;
	 private Boolean emailConfirmok;
	 private String role;
	 private Boolean isbanned;
}
