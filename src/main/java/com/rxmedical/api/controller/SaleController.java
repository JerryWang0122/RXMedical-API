package com.rxmedical.api.controller;

import com.rxmedical.api.model.dto.*;
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

	// 後台使用者，取得訂單明細
	@PostMapping("/admin/order_list/detail")
	public ResponseEntity<ApiResponse<List<OrderDetailDto>>> getOrderDetails(@RequestBody RecordDto recordDto) {
		List<OrderDetailDto> orderDetails = saleService.getOrderDetails(recordDto.getRecordId());
		if (orderDetails == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "訂單資訊不存在", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "訂單明細", orderDetails));
	}

	// 後台使用者，取得"待撿貨"訂單明細
	@PostMapping("/admin/order_list/picking/detail")
	public ResponseEntity<ApiResponse<List<HistoryProductDto>>> getHistoryProductList(@RequestBody RecordDto recordDto) {
		List<HistoryProductDto> historyProductList = saleService.getHistoryProductList(recordDto.getRecordId());
		if (historyProductList == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "訂單錯誤", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "訂單明細", historyProductList));
	}

	@PatchMapping("/admin/order_list/picking")
	public ResponseEntity<ApiResponse<String>> pickUpItem(@RequestBody PickingHistoryDto pickingHistoryDto) {
		String errorMsg = saleService.pickUpItem(pickingHistoryDto);
		if (errorMsg == null) {
			return ResponseEntity.ok(new ApiResponse<>(true, "撿貨成功", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(false, errorMsg, null));
	}

	// ---------------------------- 取得 [status] 的訂單清單 --------------------------------
	// 後台使用者，取得所有未確認訂單清單
	@PostMapping("/admin/order_list/unchecked")
	public ResponseEntity<ApiResponse<List<OrderListDto>>> getUncheckedOrderList() {
		List<OrderListDto> orderList = saleService.getUncheckedOrderList();
		return ResponseEntity.ok(new ApiResponse<>(true, "訂單資訊", orderList));
	}

	// 後台使用者，取得所有待撿貨訂單清單
	@PostMapping("/admin/order_list/picking")
	public ResponseEntity<ApiResponse<List<OrderListDto>>> getPickingOrderList() {
		List<OrderListDto> orderList = saleService.getPickingOrderList();
		return ResponseEntity.ok(new ApiResponse<>(true, "訂單資訊", orderList));
	}

	// 後台使用者，取得所有待出貨訂單清單
	@PostMapping("/admin/order_list/waiting")
	public ResponseEntity<ApiResponse<List<OrderListDto>>> getWaitingOrderList() {
		List<OrderListDto> orderList = saleService.getWaitingOrderList();
		return ResponseEntity.ok(new ApiResponse<>(true, "訂單資訊", orderList));
	}

	// 後台使用者，取得所有運送中訂單清單
	@PostMapping("/admin/order_list/transporting")
	public ResponseEntity<ApiResponse<List<OrderListDto>>> getTransportingOrderList() {
		List<OrderListDto> orderList = saleService.getTransportingOrderList();
		return ResponseEntity.ok(new ApiResponse<>(true, "訂單資訊", orderList));
	}

	// 後台使用者，取得所有已完成訂單清單
	@PostMapping("/admin/order_list/finish")
	public ResponseEntity<ApiResponse<List<OrderListDto>>> getFinishOrderList() {
		List<OrderListDto> orderList = saleService.getFinishOrderList();
		return ResponseEntity.ok(new ApiResponse<>(true, "訂單資訊", orderList));
	}

	// 後台使用者，取得所有取消訂單清單
	@PostMapping("/admin/order_list/rejected")
	public ResponseEntity<ApiResponse<List<OrderListDto>>> getRejectedOrderList() {
		List<OrderListDto> orderList = saleService.getRejectedOrderList();
		return ResponseEntity.ok(new ApiResponse<>(true, "訂單資訊", orderList));
	}



	// ---------------------------- 對status做操作 ---------------------
	// 後台把"待確認"訂單狀態往"待撿貨"狀態送
	@PutMapping("/admin/order_list/unchecked")
	public ResponseEntity<ApiResponse<String>> pushToPicking(@RequestBody RecordDto recordDto) {
		String errorMsg = saleService.pushToPicking(recordDto.getRecordId());
		if (errorMsg == null) {
			return ResponseEntity.ok(new ApiResponse<>(true, "訂單狀態更改成功", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(false, errorMsg, null));
	}

	// 後台把"待撿貨"訂單狀態往"待出貨"狀態送
	@PutMapping("/admin/order_list/picking")
	public ResponseEntity<ApiResponse<String>> pushToWaiting(@RequestBody RecordDto recordDto) {
		String errorMsg = saleService.pushToWaiting(recordDto.getRecordId());
		if (errorMsg == null) {
			return ResponseEntity.ok(new ApiResponse<>(true, "訂單狀態更改成功", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(false, errorMsg, null));
	}

	// 後台把"待確認"訂單狀態往"取消"狀態送
	@DeleteMapping("/admin/order_list/unchecked")
	public ResponseEntity<ApiResponse<String>> pushToRejected(@RequestBody RecordDto recordDto) {
		String errorMsg = saleService.pushToRejected(recordDto.getRecordId());
		if (errorMsg == null) {
			return ResponseEntity.ok(new ApiResponse<>(true, "訂單狀態更改成功", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(false, errorMsg, null));
	}

	// 後台把"待出貨"訂單狀態往"運送中"狀態送
	@PutMapping("/admin/order_list/waiting")
	public ResponseEntity<ApiResponse<String>> pushToTransporting(@RequestBody PushToTransportingDto pushToTransportingDto) {
		String errorMsg = saleService.pushToTransporting(pushToTransportingDto);
		if (errorMsg == null) {
			return ResponseEntity.ok(new ApiResponse<>(true, "訂單狀態更改成功", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(false, errorMsg, null));
	}

	// ---------------------------- 產生訂單 --------------------------
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

	//------------------------------- 進銷 ----------------------------------------
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
