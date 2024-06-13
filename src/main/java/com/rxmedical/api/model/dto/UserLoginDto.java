package com.rxmedical.api.model.dto;

/**
 * [INPUT] 使用者登入的 DTO
 * @param email 信箱
 * @param password 密碼
 * @param token CSRF TOKEN
 */
public record UserLoginDto(
					String email, 
					String password, 
					String token) {
}
