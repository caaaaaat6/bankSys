package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.util.BLLUtil;


public class OrdinaryFixedUserAccount extends OrdinaryUserAccount implements BaseFixedAccountRight {

    @Override
    public double deposit(double money, int depositDays) {
        return BLLUtil.fixedDeposit(getCardRepository(), getTradeRepository(), getCard(), money, depositDays);
    }

    @Override
    public double depositByEmployee(double money, int depositDays, Long employeeId) {
        return BLLUtil.fixedDepositByEmployee(getCardRepository(), getTradeRepository(), getCard(), money, employeeId, depositDays);
    }
}
