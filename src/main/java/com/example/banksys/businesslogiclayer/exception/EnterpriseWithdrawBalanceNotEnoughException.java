package com.example.banksys.businesslogiclayer.exception;

/**
 * 企业用户取款后余额不足门限异常
 */
public class EnterpriseWithdrawBalanceNotEnoughException extends Exception {
    public EnterpriseWithdrawBalanceNotEnoughException(String message) {
        super(message);
    }
}
