package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.response.ApiResponse;
import com.example.demo.service.impl.CurrencyService;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"},allowCredentials = "true")
public class CurrencyController {
	@Autowired
	private CurrencyService currencyService;
	
	@GetMapping("/twd-jpy")
	public ResponseEntity<ApiResponse<Double>> getTwdToJpyRate() {
        try {
            double rate = currencyService.getTwdToJpyRate();
            return ResponseEntity.ok(ApiResponse.success("TWD, JPY", rate));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(403, "EXCHANGE_RATE_ERROR"+ e.getMessage()));
           
        }
    }
}
