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
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String empCode;		// 員工識別碼ID

	@Column
	private String name;	// 姓名

	@Column
	private String salt;	// 鹽

	@Column
	private String password;  // 密碼

	@Column
	private String dept;      // 部門

	@Column
	private String title;    // 職稱

	@Column
	private String email;    // 信箱

	/**
	 *     authLevel 權限分五級
	 * 		1. off : 關閉權限
	 * 		2. register: 完成註冊
	 *   ---- (以上無法登入使用) ----
	 * 		3. staff: 一般員工
	 * 		4. admin: 管理人員
	 * 		5.  root: 最高管理者
	 */
	@Column
	private String authLevel;  // 權限

	@Column
	private Date createDate;  // 建立日期

	@Column
	private Date updateDate;  // 更新日期
}
