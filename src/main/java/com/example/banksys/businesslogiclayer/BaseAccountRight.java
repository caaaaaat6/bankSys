package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.Trade;

import java.util.Date;
import java.util.List;

// 职员权限
interface BaseAccountRight {

    //开户
    long openAccount(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney, Long employeeId);

//    // 存款
//    double deposit(double money);

    //取款
    double withdraw(double money) throws WithdrawException, EnterpriseWithdrawBalanceNotEnoughException;

    //查询余额及查询日志
    String queryBalance();

    //按范围查询
    List<Trade> queryDaybook(Date start, Date end);

    //转账
    double transferMoneyTo(Card toCard, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException;

//    //改密码
    void changePassWord();

    //销户
    void closeAccount();
}
