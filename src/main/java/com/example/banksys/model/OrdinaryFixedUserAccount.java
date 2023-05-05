package com.example.banksys.model;

public class OrdinaryFixedUserAccount extends OrdinaryUserAccount implements FixedUserAccountRight {
    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, String cardType, double openMoney) {
        return 0;
    }

    @Override
    public void save(int depositDays) {

    }
}
