package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [INPUT] 後台指定運送人員時，用來傳送資料的DTO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushToTransportingDto {
	private Integer userId;
	private Integer recordId;
	private Integer transporterId;
}
