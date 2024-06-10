package com.rxmedical.api.repository;

import com.rxmedical.api.model.po.History;
import com.rxmedical.api.model.po.Product;
import com.rxmedical.api.model.po.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Integer> {
    Integer countByRecord(Record record);
    Integer countByRecordAndUserIsNull(Record record);
    List<History> findByRecord(Record record);
    List<History> findByProductAndUpdateDateAfter(Product product, Date startDate);
}
