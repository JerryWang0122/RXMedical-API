package com.rxmedical.api.model.po;

import java.util.Date;

/**
 *
 * @param id
 * @param name
 * @param stock 庫存量
 * @param desc 商品描述
 * @param picture 圖片儲存位置
 * @param category 商品種類
 * @param createDate
 * @param updateDate
 */
public record Product(Integer id, String name, Integer stock, String desc,
                      String picture, String category, Date createDate, Date updateDate) {}
