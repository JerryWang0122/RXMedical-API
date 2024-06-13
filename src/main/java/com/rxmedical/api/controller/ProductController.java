package com.rxmedical.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rxmedical.api.model.dto.GetMaterialInfoDto;
import com.rxmedical.api.model.dto.MaterialFileUploadDto;
import com.rxmedical.api.model.dto.MaterialInfoDto;
import com.rxmedical.api.model.dto.MaterialUpdateInfoDto;
import com.rxmedical.api.model.dto.ProductItemInfoDto;
import com.rxmedical.api.model.dto.ShowMaterialsDto;
import com.rxmedical.api.model.dto.ShowProductsDto;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.service.ProductService;

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

	// 前台查詢所有產品
	@PostMapping("/product")
	public ResponseEntity<ApiResponse<List<ShowProductsDto>>> getProductList() {
		return ResponseEntity.ok(new ApiResponse<>(true, "產品資訊列表", productService.getProductList()));
	}

	@PostMapping("/product/item")
	public ResponseEntity<ApiResponse<ProductItemInfoDto>> getProductItemInfo(@RequestBody GetMaterialInfoDto getInfoDto) {
		ProductItemInfoDto info = productService.getProductItemInfo(getInfoDto.getMaterialId());
		if (info == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "產品資訊不存在", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "產品資訊", info));
	}

	// 後台查詢所有產品
	@PostMapping("/admin/material")
	public ResponseEntity<ApiResponse<List<ShowMaterialsDto>>> getMaterialList() {
		
		List<ShowMaterialsDto> materialList = productService.getMaterialList();
		
		return ResponseEntity.ok(new ApiResponse<>(true, "產品資訊", materialList));
	}

	// 後台新增商品
	@PostMapping("/admin/material/create")
	public ResponseEntity<ApiResponse<Object>> materialInfoUpload(@RequestBody MaterialFileUploadDto materialInfoDto) {
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
		
		MaterialInfoDto materialInfo = productService.getMaterialInfo(searchDto.getMaterialId());
		
		if (materialInfo == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "產品資訊不存在", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "產品資訊", materialInfo));
	}

	@PutMapping("/admin/material/edit")
	public ResponseEntity<ApiResponse<Object>> materialInfoUpdate(@RequestBody MaterialUpdateInfoDto updateInfoDto) {
		Boolean success = productService.updateMaterialInfo(updateInfoDto);
		if (success) {
			return ResponseEntity.ok(new ApiResponse<>(true, "商品更新成功", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(false, "商品更新失敗", null));
	}
}
