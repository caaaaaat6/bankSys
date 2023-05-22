package com.example.banksys.model.Exception;

/**
 * 取钱后余额不能为负异常
 */
public class WithdrawException extends Throwable {
    public WithdrawException(String s) {
        super(s);
    }
}
