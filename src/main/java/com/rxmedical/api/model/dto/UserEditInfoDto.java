package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [INPUT] 使用者個人帳戶資訊的DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditInfoDto {
	private Integer userId;
	private String name;
	private String dept;
	private String title;
	private String email;

}
