package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.Trade;

import java.util.Date;
import java.util.List;

// 职员权限
interface BaseAccountRight {

    //开户
    long openAccount(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney, Long employeeId);

//    // 存款
//    void deposit();

    //取款
    double withdraw(double money) throws WithdrawException, EnterpriseWithdrawBalanceNotEnoughException;

    //查询余额及查询日志
    double queryBalance();

    //按范围查询
    List<Trade> queryDaybook(Date start, Date end);

    //转账
    void transferMoneySelf();

//    //改密码
    void changePassWord();

    //销户
    void closeAccount();
}
