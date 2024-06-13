package com.rxmedical.api.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * [OUTPUT] 顯示建議商品購入量圖表使用的dto
 * @param startOfWeek
 * @param suggestQuantity
 */
public record CallAdviceDto(
						@JsonFormat(pattern = "MM-dd", timezone = "GMT+8") Date startOfWeek, 
						Integer suggestQuantity) {
}
