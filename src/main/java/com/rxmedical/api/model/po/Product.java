package com.rxmedical.api.model.po;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(unique = true)
    private String code;    // 產品識別號

    @Column
    private String name;    // 產品名稱

    @Column
	private Integer stock;  // 庫存量

    @Column
    private Integer safetyThreshold;  // 安全庫存

    @Column
	private String description;     // 產品描述

    @Column
    private String storage;     // 儲存位置

    @Column(columnDefinition = "LONGTEXT")
	private String picture;     // 圖片儲存位置

    @Column
	private String category;     // 商品種類

    @Column
    @CreationTimestamp(source = SourceType.DB)
	private Date createDate;     // 建立日期

    @Column
    @UpdateTimestamp(source = SourceType.DB)
	private Date updateDate;     // 更新日期
}
