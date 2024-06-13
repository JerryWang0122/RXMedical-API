package com.rxmedical.api.model.dto;

/**
 * [INPUT] 後台指定運送人員時，用來傳送資料的DTO
 */
public record PushToTransportingDto (
					    Integer userId,
					    Integer recordId,
					    Integer transporterId) {
}
