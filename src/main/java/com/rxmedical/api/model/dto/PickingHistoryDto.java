package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [INPUT] 後台使用者撿貨後，按下"我拿了"按鈕後，用來傳送資料的DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PickingHistoryDto {
	private Integer userId;
	private Integer historyId;

}
