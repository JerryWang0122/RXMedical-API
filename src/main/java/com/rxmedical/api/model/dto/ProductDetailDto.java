package com.rxmedical.api.model.dto;

/**
 * [OUTPUT] 後台撿商品時，紀錄商品資料的DTO，搭配HistoryProductDTO使用
 * @param code  產品編號
 * @param name  產品名稱
 * @param storage  產品儲存位置
 * @param picture  產品圖片
 */
public record ProductDetailDto(
						String code, 
						String name, 
						String storage, 
						String picture) {
}
