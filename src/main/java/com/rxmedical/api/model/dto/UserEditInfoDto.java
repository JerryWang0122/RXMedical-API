package com.rxmedical.api.model.dto;

/**
 * [INPUT] 使用者個人帳戶資訊的DTO
 * @param userId
 * @param name 姓名
 * @param dept 處室
 * @param title 職稱
 * @param email 信箱
 */
public record UserEditInfoDto(Integer userId, String name, String dept, String title, String email) {

}
