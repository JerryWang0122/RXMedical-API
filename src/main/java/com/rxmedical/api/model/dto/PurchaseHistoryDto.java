package com.rxmedical.api.model.dto;

/**
 * [OUTPUT] 前台使用者查詢過往申請紀錄用的Dto
 * @param id    record PK
 * @param code  申請單號
 * @param orderQty 申請品項數量
 * @param status  狀態
 */
public record PurchaseHistoryDto(
							Integer id, 
							String code, 
							Integer orderQty, 
							String status) {
}
