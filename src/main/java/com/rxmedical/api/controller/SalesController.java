package com.rxmedical.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sales/api")
public class SalesController {
	
	@GetMapping("/test")
	public String getTest() {
		return "Sales API 連接成功";
	}
	
	//
	//
	//
	//
	//
}
