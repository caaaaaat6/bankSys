package com.example.banksys.model;

import lombok.Data;

@Data
public class EnterpriseFixedUserAccount extends EnterpriseUserAccount implements FixedUserAccountRight {

    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, String cardType, double openMoney) {
        return 0;
    }

    @Override
    public void save(int depositDays) {

    }
}
