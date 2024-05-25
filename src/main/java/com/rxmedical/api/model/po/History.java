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
@Table(name = "history")
public class History {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer quantity;	// 該次進、銷變化量

	@Column
	private Integer price;		// 該次進、銷貨金額（銷統一為 0）

	@Column
	private String flow;	// 進、銷

	@ManyToOne
	private Record record;		// Record 類 -> 對應哪一筆衛材單

	@ManyToOne
	private Product product;	// Product 類 -> 對應哪一項衛材

	@ManyToOne
	private User user;		// User 類 -> 誰去取貨或進貨

	@Column
	private Date createDate;	// 建立日期

	@Column
	private Date updateDate;	// 更新日期
}
