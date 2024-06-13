package com.rxmedical.api.model.dto;

/**
 * [OUTPUT] 後台顯示安全庫存比率
 * @param name 產品名稱
 * @param code 產品編號
 * @param ratio 安全庫存比率
 */
public record SafetyRatioDto(
						String name, 
						String code, 
						Float ratio) {
}
