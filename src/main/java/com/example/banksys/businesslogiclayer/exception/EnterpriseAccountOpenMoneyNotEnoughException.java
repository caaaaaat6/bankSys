package com.example.banksys.businesslogiclayer.exception;

/**
 * 企业账户开户金额不足门限异常
 */
public class EnterpriseAccountOpenMoneyNotEnoughException extends RuntimeException {

    public EnterpriseAccountOpenMoneyNotEnoughException(String s) {
        super(s);
    }
}
