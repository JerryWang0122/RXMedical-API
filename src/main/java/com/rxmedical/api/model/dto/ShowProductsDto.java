package com.rxmedical.api.model.dto;

/**
 * [OUTPUT] 前台使用者顯示所有商品用的DTO
 * @param id    商品ID
 * @param name 商品名稱
 * @param stock 商品庫存
 * @param category  商品類別
 * @param picture   商品圖片
 */
public record ShowProductsDto(
						Integer id, 
						String name, 
						Integer stock, 
						String category, 
						String picture) {
}
