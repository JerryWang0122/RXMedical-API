package com.rxmedical.api.model.po;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**<pre>
 * Product     商品
 * ========================
 * id
 * name        商品名稱
 * stock       庫存量
 * desc        商品描述
 * picture     圖片儲存位置
 * category    商品種類
 * createDate  建立日期
 * updateDate  更新日期
 * </pre>
 */
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
    private String productId;

    @Column
    private String storage;

    @Column
	private String name;

    @Column
	private Integer stock;

    @Column
	private String description;

    @Column
	private String picture;

    @Column
	private String category;

    @Column
	private Date createDate;

    @Column
	private Date updateDate;
}
