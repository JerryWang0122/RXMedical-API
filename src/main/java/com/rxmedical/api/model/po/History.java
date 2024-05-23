package com.rxmedical.api.model.po;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {
	private Integer id;
	private Integer quantity;
	private Integer price;
	private String flow;
	private Integer recordId;
	private Integer productId;
	private Integer userId;
	private Date createDate;
	private Date updateDate;
}
