package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor(force = true)
//@Service
public class EnterpriseFixedUserAccount extends EnterpriseUserAccount implements BaseFixedAccountRight {
    @Override
    public long openEnterpriseAccount(long userId, String userPid, String userName, String password, Long enterpriseId, String cardType, double openMoney, Long employeeId) {
        return super.openEnterpriseAccount(userId, userPid, userName, password, enterpriseId, cardType, openMoney, employeeId);
    }

    @Override
    public double deposit(double money, int depositDays) {
        return BLLUtil.fixedDeposit(getCardRepository(), getTradeRepository(), getCard(), money, depositDays);
    }

    @Override
    public double depositByEmployee(double money, int depositDays, Long employeeId) {

        return BLLUtil.fixedDepositByEmployee(getCardRepository(), getTradeRepository(), getCard(), money, employeeId, depositDays);
    }

//    @Override
//    public double deposit(double money) {
//        return deposit(money, 0);
//    }

    @Override
    public String queryBalance() {
        return super.queryBalance();
    }

    @Override
    public double withdraw(double money) throws WithdrawException, EnterpriseWithdrawBalanceNotEnoughException {
        return super.withdraw(money);
    }
}
