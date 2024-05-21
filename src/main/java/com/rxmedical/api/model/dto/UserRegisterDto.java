package com.rxmedical.api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 註冊時用的資料
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    private String empId;	// 員工編號
    private String name;	// 姓名
    private String dept;	// 單位
    private String title;	// 職稱
    private String email;   // 信箱
    private String password; // 密碼
}
