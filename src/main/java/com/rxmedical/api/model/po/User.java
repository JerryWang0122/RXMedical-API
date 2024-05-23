package com.rxmedical.api.model.po;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	private Integer id;
	private String empId;
	private String name;
	private String password;
	private String dept;
	private String title;
	private String email;
	private String authLevel;
	private Date createDate;
	private Date updateDate;
}
