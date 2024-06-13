package com.rxmedical.api.model.dto;

/**
 * [INPUT] 前台使用者利用購物車送出申請訂單時，紀錄申請資訊的DTO，搭配ApplyRecordDto使用
 * @param productId 欲申請商品的id
 * @param applyQty 申請數量
 */
public record ApplyItemDto(
						Integer productId, 
						Integer applyQty) {
}
