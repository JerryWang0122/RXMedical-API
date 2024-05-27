package com.rxmedical.api.model.dto;

/**
 * [OUT] 使用者資訊的DTO
 */
//員工編號
// 姓名
// 處室
// 職稱
// 信箱
// 權限級別
public record UserInfoDto(Integer id, String empCode, String name, String dept, String title, String email, String authLevel) {

}
