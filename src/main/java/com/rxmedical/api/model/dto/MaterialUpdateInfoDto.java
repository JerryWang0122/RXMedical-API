package com.rxmedical.api.model.dto;

/**
 * [INPUT] 後台使用者，更新產品資料後，上傳資訊的DTO
 */
public record MaterialUpdateInfoDto(
							    Integer productId,
							    String name,
							    String category,
							    Integer safetyThreshold,
							    String storage,
							    String description,
							    String updatePicture,
							    Integer userId) {
}
