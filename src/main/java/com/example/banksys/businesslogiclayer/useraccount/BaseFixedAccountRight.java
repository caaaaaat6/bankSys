package com.example.banksys.businesslogiclayer.useraccount;

import org.springframework.transaction.annotation.Transactional;

/**
 * 账户的定期权限
 */
public interface BaseFixedAccountRight extends BaseAccountRight {

    /**
     * 定期存款
     * @param money 定期存款金额
     * @param depositDays 定期存款天数
     * @return 存款后余额
     */
    @Transactional
    double deposit(double money, int depositDays);

}
