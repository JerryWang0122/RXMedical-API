package com.rxmedical.api.model.po;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @param id            PK
 * @param recordId      公文號
 * @param status        衛材單狀態
 * @param demanderId    申請人
 * @param transporterId 運送人
 * @param createDate
 * @param updateDate
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {

	private Integer id;
	private String recordId;
	private String status;
	private Integer demanderId;
	private Integer transporterId;
	private Date createDate;
	private Date updateDate;
}
