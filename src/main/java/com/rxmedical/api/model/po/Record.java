package com.rxmedical.api.model.po;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Record         交易紀錄
 * ========================
 * id
 * recordId       公文號
 * status         衛材單狀態
 * demanderId     申請人
 * transporterId  運送人
 * createDate     建立日期
 * updateDate     更新日期
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "record")
public class Record {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String recordId;

	@Column
	private String status;

	@ManyToOne
	private User demander;

	@ManyToOne
	private User transporter;

	@Column
	private Date createDate;

	@Column
	private Date updateDate;
}
