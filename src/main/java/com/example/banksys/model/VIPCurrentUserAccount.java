package com.example.banksys.model;

import lombok.Data;

@Data
public class VIPCurrentUserAccount extends CurrentUserAccount implements TransferableCurrentAccountRight {

    @Override
    public void transferMoneyTo(TransferToRight transferable) {

    }

    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, String cardType, double openMoney) {
        return 0;
    }
}
