package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.businesslogiclayer.useraccount.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.BaseFixedAccountRight;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.Trade;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.form.DepositCurrentForm;
import com.example.banksys.presentationlayer.form.DepositFixedForm;
import com.example.banksys.presentationlayer.form.WithdrawForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Service {
    @Transactional
    double depositCurrent(BaseCurrentAccountRight accountRight, DepositCurrentForm form);

    @Transactional
    double depositFixed(BaseFixedAccountRight accountRight, DepositFixedForm form);

    @Transactional
    double withdraw(BaseAccount account, WithdrawForm form) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException;

    @Transactional
    String queryBalance(BaseAccount account);

    @Transactional
    List<AccountLog> queryQueryLogs(BaseAccount account);

    @Transactional
    List<Trade> queryTrades(BaseAccount account);
}
