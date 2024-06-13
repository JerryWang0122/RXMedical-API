package com.rxmedical.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * [OUTPUT] 後台顯示訂單狀態清單的DTO
 * @param id
 * @param code 訂單編號
 * @param applyAmount 申請衛材量
 * @param demander 申請人資料
 * @param transporterName 運送人員
 * @param updateDate 更新時間
 */
public record OrderListDto(
					Integer id, 
					String code, 
					Integer applyAmount, 
					OrderDemanderDto demander, 
					String transporterName, 
					@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") Date updateDate) {
}
