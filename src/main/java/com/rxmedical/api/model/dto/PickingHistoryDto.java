package com.rxmedical.api.model.dto;

/**
 * [INPUT] 後台使用者撿貨後，按下"我拿了"按鈕後，用來傳送資料的DTO
 * @param userId
 * @param historyId
 * @param verifyToken
 */
public record PickingHistoryDto(Integer userId, Integer historyId, String verifyToken) {
}
