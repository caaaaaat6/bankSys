package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.presentationlayer.form.ChangePasswordForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户Service的实现类，用户Service的基类
 */
public abstract class BaseUserService implements UserService {

    /**
     * 改密码
     * @param account 用户基类
     */
    @Override
    @Transactional
    public void changePassword(BaseAccount account, PasswordEncoder passwordEncoder, ChangePasswordForm changePasswordForm) {
        String password = changePasswordForm.getPassword();
        account.changePassword(passwordEncoder.encode(password));
    }
}
