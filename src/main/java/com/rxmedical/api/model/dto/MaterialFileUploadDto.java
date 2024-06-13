package com.rxmedical.api.model.dto;

/**
 * [INPUT] 首次上架商品時，上傳的資訊
 */
public record MaterialFileUploadDto(
							    String code,
							    String name,
							    String category,
							    String storage,
							    Integer safetyThreshold,
							    String description,
							    Integer quantity,
							    Integer price,
							    Integer userId,
							    String picture) {
}
