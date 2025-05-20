package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //Springboot 啟動完成前會先執行此配置
public class ModelMapperConfig {
	
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	
}
