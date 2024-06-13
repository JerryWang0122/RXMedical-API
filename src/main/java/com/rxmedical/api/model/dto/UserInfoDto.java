package com.rxmedical.api.model.dto;

/**
 * [OUT] 使用者資訊的DTO
 * @param id
 * @param empCode 員工編號
 * @param name 姓名
 * @param dept 處室
 * @param title 職稱
 * @param email 信箱
 * @param authLevel 權限級別
 */
public record UserInfoDto(
					Integer id, 
					String empCode, 
					String name, 
					String dept, 
					String title, 
					String email, 
					String authLevel) {
}
