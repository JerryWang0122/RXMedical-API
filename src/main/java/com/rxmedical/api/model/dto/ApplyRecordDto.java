package com.rxmedical.api.model.dto;

import java.util.List;

/**
 * [INPUT] 購物車按下申請後，用來傳送資料的DTO
 */
public record ApplyRecordDto (
							Integer userId, 
							List<ApplyItemDto> applyItems) {
}
