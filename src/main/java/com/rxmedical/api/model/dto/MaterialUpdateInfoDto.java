package com.rxmedical.api.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [INPUT] 後台使用者，更新產品資料後，上傳資訊的DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialUpdateInfoDto {
	private Integer productId;
	private String name;
	private String category;
	private Integer safetyThreshold;
	private String storage;
	private String description;
	private String updatePicture;
	private Integer userId;
}
