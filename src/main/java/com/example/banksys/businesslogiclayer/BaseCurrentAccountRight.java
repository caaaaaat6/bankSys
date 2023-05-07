package com.example.banksys.businesslogiclayer;

// 职员的活期权限
public interface BaseCurrentAccountRight extends BaseAccountRight {

    // 活期存款，不需要天数参数
    double deposit(double money);

    double depositByEmployee(double money, Long employeeId);
}
