package com.rxmedical.api.model.dto;

/**
 * [INPUT] 更改會員權限的 DTO
 */
public record ChangeMemberAuthDto(
							Integer userId,
							Integer memberId,
							String authLevel) {
}
