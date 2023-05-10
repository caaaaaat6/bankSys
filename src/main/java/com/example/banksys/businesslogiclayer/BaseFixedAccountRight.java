package com.example.banksys.businesslogiclayer;

import org.springframework.transaction.annotation.Transactional;

// 职员的定期权限
public interface BaseFixedAccountRight extends BaseAccountRight {

    // 定期存款，有参数：depositDays 存款天数
    @Transactional
    double deposit(double money, int depositDays);

    @Transactional
    double depositByEmployee(double money, int depositDays, Long employeeId);

    double queryBalance();
}
