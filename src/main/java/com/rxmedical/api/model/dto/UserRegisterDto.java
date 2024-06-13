package com.rxmedical.api.model.dto;

/**
 * [INPUT] 使用者註冊的 DTO
 * @param empCode 員工編號
 * @param name 姓名
 * @param dept 單位
 * @param title 職稱
 * @param email 信箱
 * @param password 密碼
 * @param token CSRF TOKEN
 */
public record UserRegisterDto(
						String empCode, 
						String name, 
						String dept, 
						String title, 
						String email, 
						String password, 
						String token) {
}
