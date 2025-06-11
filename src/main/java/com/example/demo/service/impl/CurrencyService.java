package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CurrencyService {
	private final ObjectMapper objectMapper = new ObjectMapper();
	public Double getTwdToJpyRate() {
		RestTemplate restTemplate=new RestTemplate();	
		String jsonStr=restTemplate.getForObject("https://tw.rter.info/capi.php", String.class);
		JsonNode jsonNode;
		try {
			jsonNode = objectMapper.readTree(jsonStr);
			
			double usdToTwd = jsonNode.get("USDTWD").get("Exrate").asDouble();
			double usdToJpy = jsonNode.get("USDJPY").get("Exrate").asDouble();
			// 1 TWD = (1 / usdToTwd) USD，再換成 JPY
			return (1 / usdToTwd) * usdToJpy;
		} catch (Exception e) {
            throw new RuntimeException("EXCHANGE_RATE_ERROR" + e.getMessage());
        }
		
	}
}
