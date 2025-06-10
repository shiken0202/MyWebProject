package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.service.impl.CurrencyService;

@SpringBootTest
public class CurrencyTest {	
	@Autowired
	private CurrencyService currencyService;
	@Test
	public void test() {
		System.out.println(currencyService.getTwdToJpyRate());
	}
}
