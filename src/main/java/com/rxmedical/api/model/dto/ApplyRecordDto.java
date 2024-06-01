package com.rxmedical.api.model.dto;

import java.util.List;

/**
 * [INPUT] 購物車按下申請後，用來傳送資料的DTO
 * @param userId
 * @param applyItems
 */
public record ApplyRecordDto(Integer userId, List<ApplyItemDto> applyItems) {
}
