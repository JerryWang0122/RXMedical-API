package com.rxmedical.api.repository;

import com.rxmedical.api.model.po.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Integer> {
}
