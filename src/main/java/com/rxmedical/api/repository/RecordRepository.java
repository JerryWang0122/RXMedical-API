package com.rxmedical.api.repository;

import com.rxmedical.api.model.po.Record;
import com.rxmedical.api.model.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Integer> {
    Boolean existsByCode(String code);
    List<Record> findByStatus(String status);
    List<Record> findByDemander(User demander);

    List<Record> findByStatusAndUpdateDateAfter(String status, Date date);
}
