package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
public class OrdinaryFixedUserAccount extends OrdinaryUserAccount implements BaseFixedAccountRight {


    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney, Long employeeId) {
        return super.openAccount(userId, userPid, userName, userType, password, enterpriseId, cardType, openMoney, employeeId);
    }

    @Override
    public double deposit(double money, int depositDays) {
        return BLLUtil.fixedDeposit(getCardRepository(), getTradeRepository(), getCard(), money, depositDays);
    }

    @Override
    public double depositByEmployee(double money, int depositDays, Long employeeId) {
        return BLLUtil.fixedDepositByEmployee(getCardRepository(), getTradeRepository(), getCard(), money, employeeId, depositDays);
    }

    @Override
    public String queryBalance() {
        return super.queryBalance();
    }

    @Override
    public double withdraw(double money) throws WithdrawException {
        return super.withdraw(money);
    }
}
