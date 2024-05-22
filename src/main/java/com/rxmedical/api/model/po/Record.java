package com.rxmedical.api.model.po;

import java.util.Date;

/**
 *
 * @param id PK
 * @param recordId 公文號
 * @param status 衛材單狀態
 * @param demanderId 申請人
 * @param transporterId 運送人
 * @param createDate
 * @param updateDate
 */
public record Record(Integer id, String recordId, String status, Integer demanderId,
                     Integer transporterId, Date createDate, Date updateDate) {}
