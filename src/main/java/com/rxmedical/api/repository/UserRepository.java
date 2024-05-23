package com.rxmedical.api.repository;

import com.rxmedical.api.model.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
