package com.rxmedical.api.controller;

import com.rxmedical.api.model.dto.MaterialFileUploadDto;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	ProductService productService;
	
	@GetMapping("/test")
	public String getTest() {
		return "Product API 連接成功";
	}

	@PostMapping("/admin/material/create")
	public ResponseEntity<ApiResponse<Object>> materialInfoUpload(@RequestParam String code,
				  @RequestParam String name, @RequestParam String category, @RequestParam String storage,
				  @RequestParam String description, @RequestParam Integer quantity, @RequestParam Integer price,
				  @RequestParam Integer userId, @RequestParam MultipartFile picture) {

		MaterialFileUploadDto materialInfoDto = new MaterialFileUploadDto(code, name, category, storage,
														description, quantity, price, userId, picture);

		Boolean success = productService.registerProduct(materialInfoDto);
		if (success) {
			return ResponseEntity.ok(new ApiResponse<>(true, "新增產品成功", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(false, "新增產品失敗", null));
	}
	//
	//
	//
	//
	//
}
