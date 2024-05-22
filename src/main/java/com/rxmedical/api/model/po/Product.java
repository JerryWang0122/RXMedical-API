package com.rxmedical.api.model.po;

import java.util.Date;

public record Product(Integer id, String name, Integer quantity, String desc,
                      String picture, String category, Date createDate, Date updateDate) {}
