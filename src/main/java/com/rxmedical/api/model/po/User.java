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
	private String empId;

	@Column
	private String name;

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
