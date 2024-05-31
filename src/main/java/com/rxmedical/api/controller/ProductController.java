package com.rxmedical.api.controller;

import com.rxmedical.api.model.dto.*;
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

	// 後台查詢所有產品
	@PostMapping("/admin/material")
	public ResponseEntity<ApiResponse<List<ShowMaterialDto>>> getMaterialList(@RequestBody CurrUserDto currUserDto) {
		
		List<ShowMaterialDto> materialList = productService.getMaterialList();
		
		return ResponseEntity.ok(new ApiResponse<>(true, "產品資訊", materialList));
	}

	// 後台新增商品
	@PostMapping("/admin/material/create")
	public ResponseEntity<ApiResponse<Object>> materialInfoUpload(
			@RequestParam Integer userId, @RequestParam String code, @RequestParam String name, 
			@RequestParam String category, @RequestParam String storage, @RequestParam String description, 
			@RequestParam Integer quantity, @RequestParam Integer price, @RequestParam MultipartFile picture) {

		// Param 轉換成 DTO
		MaterialFileUploadDto materialInfoDto = new MaterialFileUploadDto(code, name, category, storage, description, quantity, price, userId, picture);
		
		Boolean success = productService.registerProduct(materialInfoDto);
		if(!success) {
			return ResponseEntity.ok(new ApiResponse<>(false, "新增產品失敗", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "新增產品成功", null));
		// DataIntegrityViolationException 問題 (先不要處理這個 Error)
		// return ResponseEntity.ok(new ApiResponse<>(false, "產品編號重複", null));
	}

	// 後台查找單一商品資料
	@PostMapping("/admin/material/edit")
	public ResponseEntity<ApiResponse<MaterialInfoDto>> getMaterialInfo(@RequestBody GetMaterialInfoDto searchDto) {
		
		MaterialInfoDto materialInfo = productService.getMaterialInfo(searchDto.materialId());
		
		if (materialInfo == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "產品資訊不存在", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "產品資訊", materialInfo));
	}

	@PutMapping("/admin/material/edit")
	public ResponseEntity<ApiResponse<Object>> materialInfoUpdate(
			@RequestParam Integer userId, @RequestParam Integer productId, @RequestParam String name, 
			@RequestParam String category, @RequestParam String storage, @RequestParam String description, 
			@RequestParam MultipartFile picture) {

		MaterialUpdateInfoDto updateInfoDto = new MaterialUpdateInfoDto(productId, name, category, storage, description, picture);
		
		Boolean success = productService.updateMaterialInfo(updateInfoDto);
		if (success) {
			return ResponseEntity.ok(new ApiResponse<>(true, "商品更新成功", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(false, "商品更新失敗", null));
	}
}
