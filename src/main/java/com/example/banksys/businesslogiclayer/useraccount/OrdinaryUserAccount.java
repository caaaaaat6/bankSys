package com.example.banksys.businesslogiclayer.useraccount;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class OrdinaryUserAccount extends PersonalUserAccount {
    /**
     * {@inheritDoc}
     */
    @Override
    public String queryBalance() {
        return super.queryBalance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double withdraw(double money) throws WithdrawException {
        return super.withdraw(money);
    }

    /**
     * 普通个人账户只能向自己的其他账户转账
     */
    @Override
    public double transferMoneyTo(Card toCard, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        if (!isSameUser(toCard)) {
            throw new UntransferableException("普通用户只能向自己的其他账户转账！");
        }
        return super.transferMoneyTo(toCard, money);
    }

    /**
     * 判断是否为同一个用户，通过身份证号码判断
     * @param toCard
     * @return 是否为同一个用户
     */
    private boolean isSameUser(Card toCard) {
        return getCard().getUserPid().equals(toCard.getUserPid());
    }
}
