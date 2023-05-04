package com.example.banksys.model;

import lombok.Data;

@Data
public class VIPFixedUserAccount extends OrdinaryFixedUserAccount implements TransferableFixedAccountRight {

    @Override
    public void transferMoneyTo(TransferToRight transferable) {

    }
}
