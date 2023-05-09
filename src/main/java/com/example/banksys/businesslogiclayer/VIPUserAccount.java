package com.example.banksys.businesslogiclayer;

import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.log.AccountLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class VIPUserAccount extends PersonalUserAccount {

    Logger logger = LoggerFactory.getLogger(VIPUserAccount.class);

    public static final double DEGRADE_THRESHOLD = 100000;

//    @Transactional()
    @Override
    public double withdraw(double money) throws WithdrawException {
        boolean isDegrade = checkDegrade(money);
        if (isDegrade) {
            doDegrade();
        }
        double oldBalance = getPersonalCard().getBalance();
        double balance = super.withdraw(money);
        log(money, oldBalance, balance, isDegrade);
        return balance;
    }

    private void log(double money, double oldBalance, double balance, boolean isDegrade) {
        Long userId = getUser().getUserId();
        Long cardId = getPersonalCard().getCardId();
        String operationType = AccountLog.OperationType.WITHDRAW;
        StringBuilder description = new StringBuilder();

        description.append("取款金额：" + money + "元\n");
        description.append("取前金额：" + oldBalance + "元\n");
        description.append("取后金额：" + balance + "元\n");
        description.append("是否降级：" + isDegrade);

        AccountLog log = new AccountLog(userId, cardId, employeeId, operationType, description.toString());
        accountLogRepository.save(log);

        // 后台输出日志
        logger.info("[afterReturning]: ---> " + log.toString());
    }

    /**
     * 完成降级，将所有个人卡降级为ordinary，并且将用户类型降级为ordinary
     */
    private void doDegrade() {
        String userPid = getPersonalCard().getUserPid();
        List<PersonalCard> personalCardsByUserPid = getPersonalCardRepository().findPersonalCardsByUserPid(userPid).get();
        for (var personalCard : personalCardsByUserPid) {
            personalCard.setUserType(Card.UserType.ORDINARY);
        }
        getPersonalCardRepository().saveAll(personalCardsByUserPid);


        List<PersonalCard> personalCardsByUserPidTest = getPersonalCardRepository().findPersonalCardsByUserPid(userPid).get();


        getUser().setUserType(Card.UserType.ORDINARY);
        getUserRepository().save(getUser());
    }

    private boolean checkDegrade(double money) {
        String userPid = getPersonalCard().getUserPid();
        List<PersonalCard> personalCardsByUserPid = getPersonalCardRepository().findPersonalCardsByUserPid(userPid).get();
        double sumBalance = 0;
        for (var personalCard : personalCardsByUserPid) {
            sumBalance += personalCard.getBalance();
        }
        if (sumBalance - money < DEGRADE_THRESHOLD) {
            return true;
        }
        return false;
    }
}
