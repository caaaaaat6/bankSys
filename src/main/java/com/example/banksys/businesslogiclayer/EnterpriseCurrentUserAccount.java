package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.model.Exception.WithdrawException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor(force = true)
//@Service
public class EnterpriseCurrentUserAccount extends EnterpriseUserAccount implements BaseCurrentAccountRight {

    @Override
    public long openEnterpriseAccount(long userId, String userPid, String userName, String password, Long enterpriseId, String cardType, double openMoney, Long employeeId) {
        return super.openEnterpriseAccount(userId, userPid, userName, password, enterpriseId, cardType, openMoney, employeeId);
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
    public double withdraw(double money) throws WithdrawException, EnterpriseWithdrawBalanceNotEnoughException {
        return super.withdraw(money);
    }
//    @Override
//    public void transferIn(double money) {
//        deposit(money);
//    }
}
