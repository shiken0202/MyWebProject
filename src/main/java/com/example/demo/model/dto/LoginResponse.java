package com.example.demo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "使用者JWTToken")
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType="Bearer";
}
