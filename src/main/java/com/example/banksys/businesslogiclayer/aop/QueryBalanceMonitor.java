package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.BaseAccount;
import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.log.AccountLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class QueryBalanceMonitor {

    private static final Logger logger = LoggerFactory.getLogger(QueryBalanceMonitor.class);

    @Autowired
    AccountLogRepository accountLogRepository;

    @Pointcut("execution(* com.example.banksys.businesslogiclayer.BaseAccount+.queryBalance(..))")
    public void queryBalance(){}

    @AfterReturning(value = "execution(* queryBalance())", returning = "desirableBalance_balance")
    public void queryBalanceLog(JoinPoint joinPoint, String desirableBalance_balance) {
        BaseAccount account = (BaseAccount) joinPoint.getTarget();

        // 获取accountLog所需的信息
        Card card = account.getCard();
        long userId = card.getUserId();
        long cardId = card.getCardId();
        Long employeeId = account.getEmployeeId();
        String operationType = AccountLog.OperationType.QUERY;
        StringBuilder description = new StringBuilder();

        description.append("可取余额/总余额：" + desirableBalance_balance + "元");

        AccountLog accountLog = new AccountLog(userId,cardId,employeeId,operationType,description.toString());
        accountLogRepository.save(accountLog);

        logger.info(accountLog.toString());
    }
}
