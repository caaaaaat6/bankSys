package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.BaseAccount;
import com.example.banksys.businesslogiclayer.VIPUserAccount;
import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.log.AccountLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CloseAccountMonitor {

    private static final Logger logger = LoggerFactory.getLogger(CloseAccountMonitor.class);

    @Autowired
    AccountLogRepository accountLogRepository;

    @Pointcut("execution(* com.example.banksys.businesslogiclayer.BaseAccount+.closeAccount())")
    public void closeAccount() {}

    @Before("execution(* closeAccount())")
    public void closeAccountLog(JoinPoint joinPoint) {
        BaseAccount account = (BaseAccount) joinPoint.getTarget();

        // 获取accountLog所需的信息
        Long userId = account.getUser().getUserId();
        Long cardId = account.getCard().getCardId();
        String operationType = AccountLog.OperationType.CLOSE;
        StringBuilder description = new StringBuilder();

        double oldBalance = account.getCard().getBalance();
        description.append("销户前余额：" + oldBalance + "元\n");

        AccountLog log = new AccountLog(userId, cardId, account.getEmployeeId(), operationType, description.toString());
        accountLogRepository.save(log);

        // 后台输出日志
        logger.info(log.toString());
    }
}
