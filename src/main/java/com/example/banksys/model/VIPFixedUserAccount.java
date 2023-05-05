package com.example.banksys.model;

import lombok.Data;

@Data
public class VIPFixedUserAccount extends FixedUserAccount implements TransferableFixedAccountRight {

    @Override
    public void transferMoneyTo(TransferToRight transferable) {

    }
}
