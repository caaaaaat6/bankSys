package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.businesslogiclayer.useraccount.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.BaseFixedAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.EnterpriseUserAccount;
import com.example.banksys.dataaccesslayer.EnterpriseCardRepository;
import com.example.banksys.dataaccesslayer.EnterpriseRepository;
import com.example.banksys.dataaccesslayer.EnterpriseUserRepository;
import com.example.banksys.model.*;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.form.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnterpriseService extends BaseUserService implements UserService {

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

    @Override
    @Transactional
    public String queryBalance(BaseAccount account) {
        return account.queryBalance();
    }

    @Override
    @Transactional
    public List<AccountLog> queryQueryLogs(BaseAccount account) {
        return account.queryQueryLogs();
    }

    @Override
    @Transactional
    public List<Trade> queryTrades(BaseAccount account) {
        return account.queryTrades();
    }

    @Override
    @Transactional
    public double transfer(BaseAccount account, TransferForm transferForm) throws EnterpriseWithdrawBalanceNotEnoughException, UntransferableException, WithdrawException {
        User toUser = enterpriseUserRepository.findById(transferForm.getToUserId()).get();
        Card toCard = toUser.getCard();
        return account.transferMoneyTo(toCard,transferForm.getMoney());
    }

    @Override
    @Transactional
    public void changePassword(BaseAccount account, PasswordEncoder passwordEncoder, ChangePasswordForm changePasswordForm) {
        super.changePassword(account, passwordEncoder, changePasswordForm);
    }

    @Override
    @Transactional
    public double close(BaseAccount account) {
        return account.closeAccount();
    }



}
