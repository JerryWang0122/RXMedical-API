package com.rxmedical.api.model.dto;

/**
 * [INPUT] 使用者個人帳戶資訊的DTO
 */
public record UserEditInfoDto (
					    Integer userId,
					    String name,
					    String dept,
					    String title,
					    String email) {
}
