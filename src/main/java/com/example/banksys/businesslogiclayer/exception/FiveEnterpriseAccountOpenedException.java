package com.example.banksys.businesslogiclayer.exception;

/**
 * 企业用户已开够五个账户异常
 */
public class FiveEnterpriseAccountOpenedException extends RuntimeException {
    public FiveEnterpriseAccountOpenedException(String s) {
        super(s);
    }
}
