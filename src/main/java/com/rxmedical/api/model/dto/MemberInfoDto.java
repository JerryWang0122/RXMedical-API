package com.rxmedical.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * [OUT] 會員資訊的DTO
 *
 * @param id
 * @param empCode 員工編號
 * @param name  姓名
 * @param dept  處室
 * @param title 職稱
 * @param authLevel 權限
 * @param createDate 註冊時間
 */
public record MemberInfoDto(
						Integer id, 
						String empCode, 
						String name, 
						String dept, 
						String title, 
						String authLevel, 
						@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") Date createDate) {
}
