package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [INPUT] 使用者登入的 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
	
	private String email; // 信箱
	private String passwork; // 密碼
}
