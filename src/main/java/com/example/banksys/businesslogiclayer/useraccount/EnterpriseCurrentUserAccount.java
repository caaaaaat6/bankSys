package com.example.banksys.businesslogiclayer.useraccount;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.Exception.WithdrawException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
//@Service
public class EnterpriseCurrentUserAccount extends EnterpriseUserAccount implements BaseCurrentAccountRight {

    @Override
    public long openEnterpriseAccount(long userId, String userPid, String userName, String password, Long enterpriseId, String cardType, double openMoney, Employee employee) {
        return super.openEnterpriseAccount(userId, userPid, userName, password, enterpriseId, cardType, openMoney, employee);
    }

    @Override
    public double deposit(double money) {
        return BLLUtil.currentDeposit(getCardRepository(), getTradeRepository(), getCard(), money);
    }

    @Override
    public double depositByEmployee(double money, Long employeeId) {
        return BLLUtil.currentDepositByEmployee(getCardRepository(), getTradeRepository(), getCard(), money, getEmployee());
    }

    @Override
    public String queryBalance() {
        return super.queryBalance();
    }

    @Override
    public double withdraw(double money) throws WithdrawException, EnterpriseWithdrawBalanceNotEnoughException {
        return super.withdraw(money);
    }

    @Override
    public double transferMoneyTo(Card toCard, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        return super.transferMoneyTo(toCard, money);
    }

    @Override
    public double closeAccount() {
        return super.closeAccount();
    }
}
