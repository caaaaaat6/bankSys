package com.example.banksys.businesslogiclayer.useraccount;

/**
 * 账户的活期权限
 */
public interface BaseCurrentAccountRight extends BaseAccountRight {

    /**
     * 活期存款，不需要天数参数
     * @param money
     * @return 存款后余额
     */
    double deposit(double money);

}
