package com.rxmedical.api.model.po;

import java.util.Date;

/**
 *
 * @param id
 * @param quantity 該次進、銷變化量
 * @param price 該次進貨金額（銷統一為 0）
 * @param flow 進、銷
 * @param recordId 對應哪一筆衛材單
 * @param productId 對應哪一項衛材
 * @param userId 誰去取貨或進貨
 * @param createDate
 * @param updateDate
 */
public record History(Integer id, Integer quantity, Integer price, String flow,
                      Integer recordId, Integer productId, Integer userId,
                      Date createDate, Date updateDate) {
}
