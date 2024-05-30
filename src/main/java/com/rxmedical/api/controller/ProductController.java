package com.rxmedical.api.controller;

import com.rxmedical.api.model.dto.CurrUserDto;
import com.rxmedical.api.model.dto.MaterialFileUploadDto;
import com.rxmedical.api.model.dto.ShowMaterialDto;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

	@PostMapping("/admin/material")
	public ResponseEntity<ApiResponse<List<ShowMaterialDto>>> getMaterialList(@RequestBody CurrUserDto currUserDto) {
		List<ShowMaterialDto> materialList = productService.getMaterialList();
		return ResponseEntity.ok(new ApiResponse<>(true, "產品資訊", materialList));
	}

	@PostMapping("/admin/material/create")
	public ResponseEntity<ApiResponse<Object>> materialInfoUpload(@RequestParam Integer userId,
				  @RequestParam String code, @RequestParam String name, @RequestParam String category,
				  @RequestParam String storage, @RequestParam String description, @RequestParam Integer quantity,
				  @RequestParam Integer price, @RequestParam MultipartFile picture) {

		MaterialFileUploadDto materialInfoDto = new MaterialFileUploadDto(code, name, category, storage,
														description, quantity, price, userId, picture);

		try {
			Boolean success = productService.registerProduct(materialInfoDto);
			if (success) {
				return ResponseEntity.ok(new ApiResponse<>(true, "新增產品成功", null));
			}
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			return ResponseEntity.ok(new ApiResponse<>(false, "產品編號重複", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(false, "新增產品失敗", null));
	}

}
