package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.businesslogiclayer.useraccount.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.BaseFixedAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.EnterpriseUserAccount;
import com.example.banksys.dataaccesslayer.EnterpriseCardRepository;
import com.example.banksys.dataaccesslayer.EnterpriseRepository;
import com.example.banksys.dataaccesslayer.EnterpriseUserRepository;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.presentationlayer.form.DepositCurrentForm;
import com.example.banksys.presentationlayer.form.DepositFixedForm;
import com.example.banksys.presentationlayer.form.EnterpriseOpenForm;
import com.example.banksys.presentationlayer.form.WithdrawForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnterpriseService implements com.example.banksys.businesslogiclayer.service.Service {

    private EnterpriseCardRepository enterpriseCardRepository;
    private EnterpriseUserRepository enterpriseUserRepository;
    private EnterpriseRepository enterpriseRepository;

    public EnterpriseService(EnterpriseCardRepository enterpriseCardRepository, EnterpriseUserRepository enterpriseUserRepository, EnterpriseRepository enterpriseRepository) {
        this.enterpriseCardRepository = enterpriseCardRepository;
        this.enterpriseUserRepository = enterpriseUserRepository;
        this.enterpriseRepository = enterpriseRepository;
    }

    @Transactional
    public Long openAccount(EnterpriseUserAccount enterpriseUserAccount,
                            PasswordEncoder passwordEncoder,
                            EnterpriseOpenForm enterpriseOpenForm
    ) throws Exception {
        String encoded = passwordEncoder.encode(enterpriseOpenForm.getPassword());
        Enterprise enterprise = enterpriseRepository.findById(enterpriseOpenForm.getEnterpriseId()).get();
        EnterpriseUser enterpriseUser = enterpriseUserRepository.save(new EnterpriseUser(
                enterpriseOpenForm.getUserPid(),
                enterpriseOpenForm.getUserName(),
                encoded,
                enterprise
                ));
        enterpriseUserAccount.setEnterpriseUser(enterpriseUser);
        enterpriseUserAccount.setEnterprise(enterprise);
        enterpriseUserAccount.openEnterpriseAccount(
                enterpriseUser.getUserId(),
                enterpriseOpenForm.getUserPid(),
                enterpriseOpenForm.getUserName(),
                encoded,
                null,
                enterpriseOpenForm.getCardType(),
                enterpriseOpenForm.getOpenMoney(),
                null);
        return enterpriseUser.getUserId();
    }

    @Override
    @Transactional
    public double depositCurrent(BaseCurrentAccountRight accountRight, DepositCurrentForm depositCurrentForm) {
        return accountRight.deposit(depositCurrentForm.getMoney());
    }
    @Override
    @Transactional
    public double depositFixed(BaseFixedAccountRight accountRight, DepositFixedForm depositFixedForm) {
        return accountRight.deposit(depositFixedForm.getMoney(), depositFixedForm.getDepositDays());
    }

    @Override
    @Transactional
    public double withdraw(BaseAccount account, WithdrawForm form) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException {
        return account.withdraw(form.getMoney());
    }
}
