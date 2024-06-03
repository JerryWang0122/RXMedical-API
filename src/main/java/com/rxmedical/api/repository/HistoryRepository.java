package com.rxmedical.api.repository;

import com.rxmedical.api.model.po.History;
import com.rxmedical.api.model.po.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Integer> {
    Integer countByRecord(Record record);
}
