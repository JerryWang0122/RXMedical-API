package com.rxmedical.api.model.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * [INPUT] 首次上架商品時，上傳的資訊
 * @param code 產品編號
 * @param name 產品名稱
 * @param category 產品類別
 * @param storage 儲存位置
 * @param safetyThreshold 安全庫存
 * @param description 產品描述
 * @param quantity 第一次批貨數量
 * @param price 第一次批貨價格
 * @param userId 登錄資料的人
 * @param picture 產品圖片(Base64)
 * @param verifyToken 驗證碼
 */
public record MaterialFileUploadDto(String code, String name, String category, String storage, Integer safetyThreshold,
                                    String description, Integer quantity, Integer price, Integer userId,
                                    String picture, String verifyToken) {
}
