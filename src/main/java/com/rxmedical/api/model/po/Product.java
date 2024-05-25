package com.rxmedical.api.model.po;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column
    private String code;    // 產品識別號

    @Column
    private String name;    // 產品名稱

    @Column
	private Integer stock;  // 庫存量

    @Column
	private String description;     // 產品描述

    @Column
    private String storage;     // 儲存位置

    @Column
	private String picture;     // 圖片儲存位置

    @Column
	private String category;     // 商品種類

    @Column
	private Date createDate;     // 建立日期

    @Column
	private Date updateDate;     // 更新日期
}
