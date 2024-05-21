package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [OUT] 使用者資訊的DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

	private String email; // 信箱
	private String passwork; // 密碼
	
	// 下面自行處理
}
