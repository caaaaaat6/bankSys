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
public class VIPFixedUserAccount extends VIPUserAccount implements BaseFixedAccountRight {

    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney, Employee employee) {
        return super.openAccount(userId, userPid, userName, userType, password, enterpriseId, cardType, openMoney, employee);
    }

    @Override
    public double deposit(double money, int depositDays) {
        return BLLUtil.fixedDeposit(getCardRepository(), getTradeRepository(), getCard(), money, depositDays);
    }

//    @Override
//    public double depositByEmployee(double money, int depositDays, Long employeeId) {
//        return BLLUtil.fixedDepositByEmployee(getCardRepository(), getTradeRepository(), getCard(), money, getEmployee(), depositDays);
//    }

    @Override
    public String queryBalance() {
        return super.queryBalance();
    }

    @Override
    public double withdraw(double money) throws WithdrawException {
        return BLLUtil.withdrawFixedAccount(getTradeRepository(),getCardRepository(),getCard(),getEmployee(),money);
    }

    @Override
    public double transferMoneyTo(Card toCard, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        BLLUtil.checkDesirableBalanceBeforeTransfer(getTradeRepository(),getCard(),money);
        return super.transferMoneyTo(toCard, money);
    }

    @Override
    public void changePassword(String newEncodedPassword) {
        super.changePassword(newEncodedPassword);
    }

    @Override
    public double closeAccount() {
        return super.closeAccount();
    }
}
