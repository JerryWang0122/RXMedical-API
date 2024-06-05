package com.rxmedical.api.model.dto;

/**
 * [INPUT] 前台查詢個別商品資訊的DTO
 * [INPUT] 後台衛材進銷，查找對應商品ID的DTO
 * @param userId    搜索人id
 * @param materialId 搜尋商品的id
 * @param verifyToken 驗證碼
 */
public record GetMaterialInfoDto(Integer userId, Integer materialId, String verifyToken) {
}
