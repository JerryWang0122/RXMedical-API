package com.rxmedical.api.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String empId;	// 員工編號
    private String name;	// 姓名
    private String password; // 密碼
    private String dept;	// 單位
    private String title;	// 職稱
    private String email;   // 信箱
    private String authLevel; // 權限級別
}
