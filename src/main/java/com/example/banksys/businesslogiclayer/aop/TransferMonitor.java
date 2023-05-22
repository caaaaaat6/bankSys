package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 转账切面，负责产生转账日志
 */
@Aspect
@Component
public class TransferMonitor {

    private static final Logger logger = LoggerFactory.getLogger(TransferMonitor.class);

    @Autowired
    AccountLogRepository accountLogRepository;

    /**
     * 产生转账日志，日志内容：”转账金额：XX元  转前余额：XX元  转后余额：XX元  转入账户ID：XX  转入账户的转前余额：XX元  转入账户的转后余额:XX元“
     * @param proceedingJoinPoint
     * @param toCard 转入银行卡
     * @param money 转账金额
     * @return 返回转账后转出账户的余额
     * @throws Throwable 由joinPoint.proceed()抛出的
     */
    @Around("execution(* com.example.banksys.businesslogiclayer.useraccount.BaseAccount+.transferMoneyTo(..))" +
            " && args(toCard, money)")
    public Object transferLog(ProceedingJoinPoint proceedingJoinPoint, Card toCard, double money) throws Throwable {
        BaseAccount account = (BaseAccount) proceedingJoinPoint.getTarget();

        // 获取accountLog所需的信息
        Card fromCard = account.getCard();
        long userId = fromCard.getUserId();
        long cardId = fromCard.getCardId();
        Employee employee = account.getEmployee();
        String operationType = AccountLog.OperationType.TRANSFER;
        StringBuilder description = new StringBuilder();

        double oldBalance = fromCard.getBalance();
        double oldBalanceTransferIn = toCard.getBalance();
        double newBalance = (Double) proceedingJoinPoint.proceed();
        double newBalanceTransferIn = toCard.getBalance();


        description.append("转账金额：" + money + "\n");
        description.append("转前余额：" + oldBalance + "\n");
        description.append("转后余额：" + newBalance + "\n");
        description.append("转入账户ID：" + toCard.getUserId() + "\n");
        description.append("转入账户的转前余额：" + oldBalanceTransferIn + "\n");
        description.append("转入账户的转后余额：" + newBalanceTransferIn + "\n");

        AccountLog accountLog = new AccountLog(userId,cardId,employee,operationType,description.toString());
        accountLogRepository.save(accountLog);

        logger.info(accountLog.toString());

        return newBalance;
    }
}
