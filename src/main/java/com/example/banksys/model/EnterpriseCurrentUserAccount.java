package com.example.banksys.model;

import lombok.Data;

@Data
public class EnterpriseCurrentUserAccount extends OrdinaryCurrentUserAccount implements TransferableCurrentAccountRight {

    @Override
    public void transferMoneyTo(TransferToRight transferable) {

    }
}
