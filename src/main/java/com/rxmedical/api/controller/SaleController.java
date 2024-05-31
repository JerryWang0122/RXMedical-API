package com.rxmedical.api.controller;

import com.rxmedical.api.model.dto.SaleMaterialDto;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/sales")
public class SaleController {

	@Autowired
	private SaleService saleService;
	@GetMapping("/test")
	public String getTest() {
		return "Sales API 連接成功";
	}

	// 後台使用者，進貨
	@PostMapping("/admin/call")
	public ResponseEntity<ApiResponse<Integer>> callMaterial(@RequestBody SaleMaterialDto saleDto) {
		Integer currStock = saleService.callMaterial(saleDto);
		if (currStock == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "無此商品", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "最新庫存", currStock));
	}

	// 後台使用者，銷毀貨
	@PostMapping("/admin/destroy")
	public ResponseEntity<ApiResponse<Integer>> destroyMaterial(@RequestBody SaleMaterialDto destroyDto) {
		Integer currStock = saleService.destroyMaterial(destroyDto);
		if (currStock == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "無此商品", null));
		} else if (currStock < 0) {
			return ResponseEntity.ok(new ApiResponse<>(false, "庫存不足", -currStock));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "最新庫存", currStock));
	}


}
