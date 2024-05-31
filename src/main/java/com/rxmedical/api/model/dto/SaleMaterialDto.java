package com.rxmedical.api.model.dto;

/**
 * [INPUT] 後台進/銷貨時，輸入資訊的DTO
 * @param userId
 * @param materialId
 * @param quantity
 * @param price
 */
public record SaleMaterialDto(Integer userId, Integer materialId, Integer quantity, Integer price) {
}
