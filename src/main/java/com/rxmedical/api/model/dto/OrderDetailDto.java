package com.rxmedical.api.model.dto;

/**
 * [OUTPUT]顯示訂單詳細資料，每一個商品需要拿多少、誰拿的，的DTO
 * 唯獨"待撿貨"需要用PickingDetailDto
 * @param productName 產品名稱
 * @param quantity 需要數量
 * @param takerName 撿貨的人
 */
public record OrderDetailDto(
						String productName, 
						Integer quantity, 
						String takerName) {
}
