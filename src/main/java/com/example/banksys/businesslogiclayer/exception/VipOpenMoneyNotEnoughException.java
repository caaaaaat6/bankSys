package com.example.banksys.businesslogiclayer.exception;

import lombok.Data;

/**
 * vip开户金额不足异常
 */
@Data
public class VipOpenMoneyNotEnoughException extends Exception {
    private String message;
    public VipOpenMoneyNotEnoughException(String s) {
        super(s);
        this.message = s;
    }

}
