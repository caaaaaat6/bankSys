package com.example.banksys.businesslogiclayer.exception;

/**
 * 企业银行卡已存在异常，无法为该企业继续开更多的银行卡
 */
public class EnterpriseCardExistException extends RuntimeException {
    public EnterpriseCardExistException(String s) {
        super(s);
    }
}
