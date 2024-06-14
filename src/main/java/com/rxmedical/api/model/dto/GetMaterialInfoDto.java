package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [INPUT] 前台查詢個別商品資訊的DTO
 * [INPUT] 後台衛材進銷，查找對應商品ID的DTO
 * [INPUT] 產生進貨建議趨勢圖時，查詢的DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMaterialInfoDto {
	private Integer userId;
	private Integer materialId;
}
