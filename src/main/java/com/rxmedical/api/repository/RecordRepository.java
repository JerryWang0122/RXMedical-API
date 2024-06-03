package com.rxmedical.api.repository;

import com.rxmedical.api.model.po.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Integer> {
    Boolean existsByCode(String code);
    List<Record> findByStatus(String status);
}
