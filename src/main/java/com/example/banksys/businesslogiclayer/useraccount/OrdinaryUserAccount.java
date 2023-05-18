package com.example.banksys.businesslogiclayer.useraccount;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OrdinaryUserAccount extends PersonalUserAccount {

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
        if (!isSameUser(toCard)) {
            throw new UntransferableException("普通用户只能向自己的其他账户转账！");
        }
        return super.transferMoneyTo(toCard, money);
    }

    private boolean isSameUser(Card toCard) {
        return getCard().getUserPid().equals(toCard.getUserPid());
    }
}
