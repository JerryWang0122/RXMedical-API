package com.rxmedical.api.model.dto;

/**
 * [OUTPUT] 後台查找後，回傳的單一商品詳細資料 DTO
 * @param id  商品PK
 * @param code 商品編號
 * @param name 商品名稱
 * @param category 商品類別
 * @param safetyThreshold 安全庫存
 * @param storage 儲存位置
 * @param description 商品描述
 * @param picture 商品圖片儲存位置
 */
public record MaterialInfoDto(
						Integer id, 
						String code, 
						String name, 
						String category, 
						Integer safetyThreshold, 
						String storage, 
						String description, 
						String picture) {
}
