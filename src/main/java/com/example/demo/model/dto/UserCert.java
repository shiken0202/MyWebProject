package com.example.demo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
@AllArgsConstructor
@Getter
@Data
@ToString
@Schema(description = "登入中的使用者資訊")
public class UserCert {
	@Schema(description = "使用者ID", requiredMode = Schema.RequiredMode.REQUIRED)
	private Long userId;
	@Schema(description = "使用者帳號",example = "testuser1234", requiredMode = Schema.RequiredMode.REQUIRED)
	private String userName;
	@Schema(description = "信箱認證狀態",example = "true",requiredMode =Schema.RequiredMode.REQUIRED )
	private boolean emailcheck;
	@Schema(description = "使用者角色",example = "買家",requiredMode =Schema.RequiredMode.REQUIRED )
	private String role;
	@Schema(description = "使用者狀態",example = "被禁用(true)",requiredMode =Schema.RequiredMode.REQUIRED )
	private boolean isbanned;
}
