package com.example.banksys.model;

import com.example.banksys.model.Exception.WithdrawalException;

import java.util.Date;
import java.util.List;

// 职员权限
interface BaseAccountRight {

    //开户
    long openAccount(long userId, String userPid, String userName, String userType, String password, String cardType, double openMoney);

//    // 存款
//    void deposit();

    //取款
    double withdrawal(double money) throws WithdrawalException;

    //查询余额及查询日志
    double queryBalance();

    //按范围查询
    List<Trade> queryDaybook(Date start, Date end);

    //转账
    void transferMoneySelf();

//    //改密码
//    void changePassWord();

    //销户
    void closeAccount();
}
