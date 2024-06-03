package com.rxmedical.api.model.dto;

/**
 * [INPUT] 使用者查詢訂單詳細資料用的DTO
 * [INPUT] 使用者推移訂單狀態用的DTO
 * @param userId 操作人
 * @param recordId 要找/操作的訂單編號
 */
public record RecordDto(Integer userId, Integer recordId) {
}
