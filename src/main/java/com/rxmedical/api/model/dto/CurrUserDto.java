package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [INPUT] 當前使用者想用自己的權限做某些事情時，使用的DTO
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrUserDto {
	private Integer userId;
}
