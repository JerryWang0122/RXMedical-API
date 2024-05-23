package com.rxmedical.api.repository;

import com.rxmedical.api.model.po.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Integer> {
}
