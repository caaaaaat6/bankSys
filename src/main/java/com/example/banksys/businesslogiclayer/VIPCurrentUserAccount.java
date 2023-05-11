package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.model.Card;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force = true)
public class VIPCurrentUserAccount extends VIPUserAccount implements BaseCurrentAccountRight {

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
}
