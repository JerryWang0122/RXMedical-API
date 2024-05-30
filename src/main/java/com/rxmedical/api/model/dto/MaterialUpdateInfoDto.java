package com.rxmedical.api.model.dto;


import org.springframework.web.multipart.MultipartFile;

/**
 * [INPUT] 後台使用者，更新產品資料後，上傳資訊的DTO
 * @param productId
 * @param name
 * @param category
 * @param storage
 * @param description
 * @param updatePicture
 */
public record MaterialUpdateInfoDto(Integer productId, String name, String category, String storage,
                                    String description, MultipartFile updatePicture) {
}
