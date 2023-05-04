package com.example.banksys.model;

// 可以向同类用户转账的权限
public interface TransferToRight {

    void transferMoneyTo(TransferToRight transferable);
}
