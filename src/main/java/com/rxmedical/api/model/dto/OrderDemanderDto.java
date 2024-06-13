package com.rxmedical.api.model.dto;

/**
 * [OUTPUT] 後台顯示訂單申請人資料的DTO
 * @param dept 申請人單位
 * @param title 申請人職稱
 * @param name 申請人姓名
 */
public record OrderDemanderDto(
							String dept, 
							String title, 
							String name) {
}
