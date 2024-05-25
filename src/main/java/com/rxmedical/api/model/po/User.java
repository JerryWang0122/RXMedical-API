package com.rxmedical.api.model.po;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**<pre>
 * User        使用者
 * ======================
 * id
 * name        名稱
 * salt        鹽巴
 * password    密碼
 * dept        部門
 * title       職位
 * empId       員工代號
 * email       信箱
 * authLevel   權限
 * createDate  建立日期
 * updateDate  更新日期
 * </pre>
 */
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
	private String empId;

	@Column
	private String name;

	@Column
	private String salt;

	@Column
	private String password;

	@Column
	private String dept;

	@Column
	private String title;

	@Column
	private String email;

	@Column
	private String authLevel;

	@Column
	private Date createDate;

	@Column
	private Date updateDate;
}
