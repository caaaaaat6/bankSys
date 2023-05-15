package com.example.banksys.config;

public class AccountIdNotFoundException extends RuntimeException {
    public AccountIdNotFoundException(String s) {
        super(s);
    }
}
