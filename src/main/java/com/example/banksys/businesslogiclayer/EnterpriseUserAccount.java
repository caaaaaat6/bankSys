package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.dataaccesslayer.EnterpriseCardRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseCard;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.log.AccountLog;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor(force = true)
@Service
public abstract class EnterpriseUserAccount extends BaseAccount {

    protected EnterpriseUser enterpriseUser;

    protected EnterpriseCard enterpriseCard;

    protected Enterprise enterprise;

    public static final double BALANCE_THRESHOLD = 10000;

    Logger logger = LoggerFactory.getLogger(EnterpriseUserAccount.class);


    @Autowired
    protected EnterpriseCardRepository enterpriseCardRepository;

    public long openEnterpriseAccount(long userId, String userPid, String userName, String password, Long enterpriseId, String cardType, double openMoney, Long employeeId) {
        enterpriseCard = new EnterpriseCard(userId, userPid, userName, Card.UserType.ENTERPRISE, password, enterpriseId, cardType, openMoney);
        long cardId = cardRepository.save(enterpriseCard).getCardId();
        return cardId;
    }

    @Override
    public double withdraw(double money) throws WithdrawException, EnterpriseWithdrawBalanceNotEnoughException {
        setCard(enterpriseCard);
        if (!canWithdraw(money)) {
            throw new EnterpriseWithdrawBalanceNotEnoughException("企业用户剩余存款余额不足" + BALANCE_THRESHOLD + "元，无法取款！");
        }
        double oldBalance = getEnterpriseCard().getBalance();
        double newBalance =  super.withdraw(money);
        log(money, oldBalance, newBalance);
        return newBalance;
    }

    private boolean canWithdraw(double money) {
        double balance = getEnterpriseCard().getBalance();
        if (balance - money >= BALANCE_THRESHOLD) {
            return true;
        }
        return false;
    }

    private void log(double money, double oldBalance, double balance) {
        Long userId = getEnterpriseUser().getUserId();
        Long cardId = getEnterpriseCard().getCardId();
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
