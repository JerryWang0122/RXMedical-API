package com.rxmedical.api.model.po;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * id
 * quantity 該次進、銷變化量
 * price 該次進貨金額（銷統一為 0）
 * flow 進、銷
 * recordId 對應哪一筆衛材單
 * productId 對應哪一項衛材
 * userId 誰去取貨或進貨
 * createDate
 * updateDate
 */
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
	private Integer quantity;

	@Column
	private Integer price;

	@Column
	private String flow;

	@Column
	private Integer recordId;

	@Column
	private Integer productId;

	@Column
	private Integer userId;

	@Column
	private Date createDate;

	@Column
	private Date updateDate;
}
