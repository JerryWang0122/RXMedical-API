package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [INPUT] 首次上架商品時，上傳的資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialFileUploadDto {
	private String code;
	private String name;
	private String category;
	private String storage;
	private Integer safetyThreshold;
	private String description;
	private Integer quantity;
	private Integer price;
	private Integer userId;
	private String picture;
}
