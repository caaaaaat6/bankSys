package com.example.banksys.model;

import java.util.Date;

// 职员的定期权限
public interface BaseFixedAccountRight extends BaseAccountRight {

    // 定期存款，有参数：depositDays 存款天数
    void save(int depositDays);
}
