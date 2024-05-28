package com.rxmedical.api.model.dto;

/**
 * [OUT] 使用者資訊的DTO
 */
// 姓名
// 處室
// 職稱
// 信箱
public record UserEditInfoDto(Integer id, String name, String dept, String title, String email) {

}
