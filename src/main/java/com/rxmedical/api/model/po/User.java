package com.rxmedical.api.model.po;

public record User(Integer id, String empId, String name, String password,
                   String dept, String title, String email, String authLevel){}
