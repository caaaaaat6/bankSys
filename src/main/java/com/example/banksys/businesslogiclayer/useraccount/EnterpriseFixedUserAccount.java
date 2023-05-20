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
public class EnterpriseFixedUserAccount extends EnterpriseUserAccount implements BaseFixedAccountRight {

    /**
     * 没有advice的提示，但是能够织入，IDEA有bug，日志还是能正常写入数据库
     */
    @Override
    public long openEnterpriseAccount(long userId, String userPid, String userName, String password, Long enterpriseId, String cardType, double openMoney, Employee employee) {
        return super.openEnterpriseAccount(userId, userPid, userName, password, enterpriseId, cardType, openMoney, employee);
    }

    @Override
    public double deposit(double money, int depositDays) {
        return BLLUtil.fixedDeposit(getCardRepository(), getTradeRepository(), getCard(), money, depositDays);
    }

    @Override
    public double depositByEmployee(double money, int depositDays, Long employeeId) {
        return BLLUtil.fixedDepositByEmployee(getCardRepository(), getTradeRepository(), getCard(), money, getEmployee(), depositDays);
    }

    @Override
    public String queryBalance() {
        return super.queryBalance();
    }

    @Override
    public double withdraw(double money) throws WithdrawException, EnterpriseWithdrawBalanceNotEnoughException {
        if (!canWithdraw(money)) {
            throw new EnterpriseWithdrawBalanceNotEnoughException("企业用户剩余存款余额不足" + BALANCE_THRESHOLD + "元，无法取款！");
        }
        return BLLUtil.withdrawFixedAccount(getTradeRepository(),getCardRepository(),getCard(),getEmployee(),money);
    }

    @Override
    public double transferMoneyTo(Card toCard, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        BLLUtil.checkDesirableBalanceBeforeTransfer(getTradeRepository(),getCard(),money);
        return super.transferMoneyTo(toCard, money);
    }

    @Override
    public void changePassword(String newPassword) {
        super.changePassword(newPassword);
    }

    @Override
    public double closeAccount() {
        return super.closeAccount();
    }
}
