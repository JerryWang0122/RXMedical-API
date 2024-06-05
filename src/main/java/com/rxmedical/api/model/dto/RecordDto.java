package com.rxmedical.api.model.dto;

/**
 * [INPUT] 使用者查詢訂單詳細資料用的DTO
 * [INPUT] 使用者推移訂單狀態用的DTO
 * [INPUT] 使用者查詢待撿貨狀態時的DTO
 * @param userId 操作人
 * @param recordId 要找/操作的訂單編號
 * @param verifyToken 驗證碼
 */
public record RecordDto(Integer userId, Integer recordId, String verifyToken) {
}
