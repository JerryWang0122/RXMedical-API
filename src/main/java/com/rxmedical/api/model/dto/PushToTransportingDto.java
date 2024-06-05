package com.rxmedical.api.model.dto;

/**
 * [INPUT] 後台指定運送人員時，用來傳送資料的DTO
 * @param userId  操作人
 * @param recordId  要操作的訂單
 * @param transporterId 運送人員
 * @param verifyToken 驗證碼
 */
public record PushToTransportingDto(Integer userId, Integer recordId, Integer transporterId, String verifyToken) {
}
