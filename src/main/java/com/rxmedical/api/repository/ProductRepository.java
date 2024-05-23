package com.rxmedical.api.repository;

import com.rxmedical.api.model.po.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
