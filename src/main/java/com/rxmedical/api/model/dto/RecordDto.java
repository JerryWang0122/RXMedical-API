package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [INPUT] 使用者查詢訂單詳細資料用的DTO
 * [INPUT] 使用者推移訂單狀態用的DTO
 * [INPUT] 使用者查詢待撿貨狀態時的DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDto {
	private Integer userId;
	private Integer recordId;
}
