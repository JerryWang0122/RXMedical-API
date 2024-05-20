package com.rxmedical.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/api")
public class ProductController {
	
	@GetMapping("/test")
	public String getTest() {
		return "Product API 連接成功";
	}
	
	//
	//
	//
	//
	//
	//
}
