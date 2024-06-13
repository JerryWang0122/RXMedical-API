package com.rxmedical.api.model.dto;

/**
 * [OUTPUT] 後台顯示運送人員(admin)資料的DTO
 * @param id
 * @param empCode
 * @param name
 */
public record TransporterDto(
						Integer id, 
						String empCode, 
						String name) {
}
