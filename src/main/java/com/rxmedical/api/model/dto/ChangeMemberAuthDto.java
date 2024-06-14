package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [INPUT] 更改會員權限的 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMemberAuthDto {
	private Integer userId;
	private Integer memberId;
	private String authLevel;
}
