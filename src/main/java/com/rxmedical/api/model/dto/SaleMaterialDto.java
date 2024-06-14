package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [INPUT] 後台進/銷貨時，輸入資訊的DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleMaterialDto {
	private Integer userId;
	private Integer materialId;
	private Integer quantity;
	private Integer price;
}
