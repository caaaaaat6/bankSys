package com.example.banksys.model;

// 活期账户权限，有改密码权限
public interface CurrentUserAccountRight extends BaseCurrentAccountRight, ChangePasswordRight{
}
