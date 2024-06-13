package com.rxmedical.api.model.dto;

/**
 * [OUTPUT] 後台衛材進銷，顯示目前所有商品的DTO
 * @param id
 * @param code 產品編號
 * @param name 產品名稱
 * @param stock 庫存量
 * @param storage 儲存位置
 * @param category 產品類別
 */
public record ShowMaterialsDto(
						Integer id, 
						String code, 
						String name, 
						Integer stock, 
						String storage, 
						String category) {
}
