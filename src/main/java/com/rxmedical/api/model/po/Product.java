package com.rxmedical.api.model.po;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @param id
 * @param name
 * @param stock      庫存量
 * @param desc       商品描述
 * @param picture    圖片儲存位置
 * @param category   商品種類
 * @param createDate
 * @param updateDate
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	private Integer id;
	private String name;
	private Integer stock;
	private String desc;
	private String picture;
	private String category;
	private Date createDate;
	private Date updateDate;
}
