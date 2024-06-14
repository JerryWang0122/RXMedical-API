package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * [INPUT] 購物車按下申請後，用來傳送資料的DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyRecordDto {
	private Integer userId;
	private List<ApplyItemDto> applyItems;
}
