package com.rxmedical.api.model.dto;

/**
 * [OUTPUT] 後台顯示訂單狀態清單的DTO
 * @param id
 * @param code 訂單編號
 * @param applyAmount 申請衛材量
 * @param demander 申請人資料
 */
public record OrderListDto(Integer id, String code, Integer applyAmount, OrderDemanderDto demander) {
}
