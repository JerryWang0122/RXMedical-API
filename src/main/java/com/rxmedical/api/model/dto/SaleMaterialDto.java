package com.rxmedical.api.model.dto;

/**
 * [INPUT] 後台進/銷貨時，輸入資訊的DTO
 */
public record SaleMaterialDto (
					    Integer userId,
					    Integer materialId,
					    Integer quantity,
					    Integer price) {
}
