package com.rxmedical.api.model.dto;

// 註冊時用的資料
//員工編號
// 姓名
// 單位
// 職稱
// 信箱
// 密碼
public record UserRegisterDto(String empCode, String name, String dept, String title, String email, String password) {

}
