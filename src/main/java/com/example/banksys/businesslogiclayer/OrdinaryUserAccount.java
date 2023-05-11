package com.example.banksys.businesslogiclayer;

import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.log.AccountLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OrdinaryUserAccount extends PersonalUserAccount {

    Logger logger = LoggerFactory.getLogger(OrdinaryUserAccount.class);

    @Override
    public String queryBalance() {
        return super.queryBalance();
    }

    @Override
    public double withdraw(double money) throws WithdrawException {
        double oldBalance = getPersonalCard().getBalance();
        double balance = super.withdraw(money);
//        log(money, oldBalance, balance);
        return balance;
    }

    private void log(double money, double oldBalance, double balance) {
        Long userId = getUser().getUserId();
        Long cardId = getPersonalCard().getCardId();
        String operationType = AccountLog.OperationType.WITHDRAW;
        StringBuilder description = new StringBuilder();

        description.append("取款金额：" + money + "元\n");
        description.append("取前金额：" + oldBalance + "元\n");
        description.append("取后金额：" + balance + "元\n");

        AccountLog log = new AccountLog(userId, cardId, employeeId, operationType, description.toString());
        accountLogRepository.save(log);

        // 后台输出日志
        logger.info("[afterReturning]: ---> " + log.toString());
    }
}
