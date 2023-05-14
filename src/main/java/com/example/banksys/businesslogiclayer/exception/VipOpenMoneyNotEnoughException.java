package com.example.banksys.businesslogiclayer.exception;

import lombok.Data;

@Data
public class VipOpenMoneyNotEnoughException extends Exception {
    private String message;
    public VipOpenMoneyNotEnoughException(String s) {
        super(s);
        this.message = s;
    }

}
