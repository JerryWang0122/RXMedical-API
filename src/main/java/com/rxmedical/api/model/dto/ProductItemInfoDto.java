package com.rxmedical.api.model.dto;

/**
 * [OUTPUT] 前台查詢個別商品資訊返回的DTO
 * @param id  商品id
 * @param name  商品名稱
 * @param category  商品類別
 * @param stock  商品庫存
 * @param description  商品描述
 * @param picture  商品圖片
 */
public record ProductItemInfoDto(
							Integer id, 
							String name, 
							String category, 
							Integer stock, 
							String description, 
							String picture) {
}
