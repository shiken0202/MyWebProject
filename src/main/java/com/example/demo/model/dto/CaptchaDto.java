package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CaptchaDto {
    private String captchaId;
    private String captchaImageBase64;
}
