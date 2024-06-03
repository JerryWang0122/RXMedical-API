package com.rxmedical.api.model.dto;

/**
 * [INPUT] 使用者查詢訂單詳細資料用的DTO
 * @param userId 操作人
 * @param recordId 要找的訂單編號
 */
public record GetOrderDetailDto(Integer userId, Integer recordId) {
}
