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
@Table(name = "record")
public class Record {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String code;	// 文件公文號

	@Column
	private String status;		// 訂單狀態
	/*
		待確認(unchecked:) 訂單剛送出但尚未被接受
		待撿貨(picking): 訂單接受，但尚未收集完成
		待出貨(waiting): 收集完成，等待指定配送
		運送中(transporting): admin開始配送
		已完成(finish): 配送完成
		取消(rejected): 取消
	 */

	@ManyToOne
	private User demander;		// User 類 -> 申請人

	@ManyToOne
	private User transporter;	// User 類 -> 運送人

	@Column
	private Date createDate;	// 建立日期

	@Column
	private Date updateDate;	// 更新日期
}
