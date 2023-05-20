package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.presentationlayer.form.ChangePasswordForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseUserService implements UserService {
    @Override
    @Transactional
    public void changePassword(BaseAccount account, PasswordEncoder passwordEncoder, ChangePasswordForm changePasswordForm) {
        String password = changePasswordForm.getPassword();
        account.changePassword(passwordEncoder.encode(password));
    }
}
