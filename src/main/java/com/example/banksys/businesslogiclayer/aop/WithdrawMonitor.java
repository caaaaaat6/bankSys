package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.BaseAccount;
import com.example.banksys.businesslogiclayer.VIPUserAccount;
import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.log.AccountLog;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WithdrawMonitor {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawMonitor.class);

    @Autowired
    AccountLogRepository accountLogRepository;

    @Pointcut(value = "execution(* com.example.banksys.businesslogiclayer.BaseAccount+.withdraw(..))")
    public void withdraw() {

    }

    @Around(value = "execution(* withdraw(..)) && args(money)")
    public Object beforeWithdraw(ProceedingJoinPoint proceedingJoinPoint, double money) throws Throwable {
        BaseAccount account = (BaseAccount) proceedingJoinPoint.getTarget();

        Long userId = account.getUser().getUserId();
        Long cardId = account.getCard().getCardId();
        String operationType = AccountLog.OperationType.WITHDRAW;
        StringBuilder description = new StringBuilder();

        double oldBalance = account.getCard().getBalance();
        double newBalance = (double) proceedingJoinPoint.proceed();

        description.append("取款金额：" + money + "元\n");
        description.append("取前金额：" + oldBalance + "元\n");
        description.append("取后金额：" + newBalance + "元\n");
        if (account instanceof VIPUserAccount) {
            boolean isDegrade = account.getCard().getUserType().equals(Card.UserType.ORDINARY);
            description.append("是否降级：" + isDegrade);
        }

        AccountLog log = new AccountLog(userId, cardId, account.getEmployee(), operationType, description.toString());
        accountLogRepository.save(log);

        // 后台输出日志
        logger.info("[afterReturning]: ---> " + log.toString());

        return newBalance;
    }
}
