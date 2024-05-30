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
@Table(name = "history")
public class History {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private Integer quantity;	// 該次進、銷變化量

	@Column
	private Integer price;		// 該次進、銷貨金額（售統一為 0）

	@Column
	private String flow;	// 進、銷、售

	@ManyToOne
	private Record record;		// Record 類 -> 對應哪一筆衛材單

	@ManyToOne
	private Product product;	// Product 類 -> 對應哪一項衛材

	@ManyToOne
	private User user;		// User 類 -> 誰去取貨或進貨

	@Column
	@CreationTimestamp(source = SourceType.DB)
	private Date createDate;	// 建立日期

	@Column
	@UpdateTimestamp(source = SourceType.DB)
	private Date updateDate;	// 更新日期
}
