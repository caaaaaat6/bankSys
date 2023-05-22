package com.example.banksys.businesslogiclayer.exception;

/**
 * 无法转账异常，由需求所定
 */
public class UntransferableException extends Throwable {
    public UntransferableException(String s) {
        super(s);
    }
}
