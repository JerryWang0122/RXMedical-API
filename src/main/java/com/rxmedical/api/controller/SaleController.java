package com.rxmedical.api.controller;

import com.rxmedical.api.model.dto.ApplyRecordDto;
import com.rxmedical.api.model.dto.CurrUserDto;
import com.rxmedical.api.model.dto.OrderListDto;
import com.rxmedical.api.model.dto.SaleMaterialDto;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

	@PostMapping("/admin/order_list/unchecked")
	public ResponseEntity<ApiResponse<List<OrderListDto>>> getUncheckedOrderList(@RequestBody CurrUserDto currUserDto) {
		List<OrderListDto> orderList = saleService.getUncheckedOrderList();
		return ResponseEntity.ok(new ApiResponse<>(true, "訂單資訊", orderList));
	}

	// 前台使用者，申請，產生訂單
	@PostMapping("/order_generate")
	public ResponseEntity<ApiResponse<String>> orderGenerate(@RequestBody ApplyRecordDto recordDto) {
		String errorMessage = saleService.checkOrder(recordDto);
		if (errorMessage == null) {
			return ResponseEntity.ok(new ApiResponse<>(true, "訂單申請成功", null));
		}
		switch (errorMessage) {
			case "貨號不存在":
			case "沒有申請項目":
				return ResponseEntity.ok(new ApiResponse<>(false, "訂單錯誤", null));
			default:
				return ResponseEntity.ok(new ApiResponse<>(false, "庫存不足，請修改或刪除", errorMessage));
		}
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
