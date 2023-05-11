package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;

public class OrdinaryCurrentUserAccount extends OrdinaryUserAccount implements BaseCurrentAccountRight {

    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney, Long employeeId) {
        return super.openAccount(userId, userPid, userName, userType, password, enterpriseId, cardType, openMoney, employeeId);
    }

    @Override
    public double deposit(double money) {
        return BLLUtil.currentDeposit(getCardRepository(), getTradeRepository(), getCard(), money);
    }

    @Override
    public double depositByEmployee(double money, Long employeeId) {
        return BLLUtil.currentDepositByEmployee(getCardRepository(), getTradeRepository(), getCard(), money, employeeId);
    }

    @Override
    public String queryBalance() {
        return super.queryBalance();
    }

    @Override
    public double withdraw(double money) throws WithdrawException {
        return super.withdraw(money);
    }

    @Override
    public double transferMoneyTo(Card toCard, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        return super.transferMoneyTo(toCard, money);
    }
}
