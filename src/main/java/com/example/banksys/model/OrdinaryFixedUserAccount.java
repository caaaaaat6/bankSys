package com.example.banksys.model;

public class OrdinaryFixedUserAccount extends FixedUserAccount{
    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, String cardType, double openMoney) {
        return 0;
    }
}
