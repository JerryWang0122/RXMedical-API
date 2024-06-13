package com.rxmedical.api.model.dto;

/**
 * [OUTPUT] 後台使用者，撿取商品時，紀錄資料的DTO
 * @param id History PK
 * @param quantity 撿取數量
 * @param product 產品資訊
 * @param takerName 撿貨人
 */
public record HistoryProductDto(
							Integer id, 
							Integer quantity, 
							ProductDetailDto product, 
							String takerName) {
}
