package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 查询余额的切面，负责产生查询余额的日志
 */
@Aspect
@Component
public class QueryBalanceMonitor {

    private static final Logger logger = LoggerFactory.getLogger(QueryBalanceMonitor.class);

    @Autowired
    private AccountLogRepository accountLogRepository;

    /**
     * 查询余额的pointcut
     */
    @Pointcut("execution(* com.example.banksys.businesslogiclayer.useraccount.BaseAccount+.queryBalance(..))")
    public void queryBalance(){}

    /**
     * 查询余额sfterReturning advice，日志内容：”可取余额/总余额：XX/XX元“
     * @param joinPoint
     * @param desirableBalance_balance 可取余额
     */
    @AfterReturning(value = "execution(* queryBalance())", returning = "desirableBalance_balance")
    public void queryBalanceLog(JoinPoint joinPoint, String desirableBalance_balance) {
        BaseAccount account = (BaseAccount) joinPoint.getTarget();

        // 获取accountLog所需的信息
        Card card = account.getCard();
        long userId = card.getUserId();
        long cardId = card.getCardId();
        Employee employee = account.getEmployee();
        String operationType = AccountLog.OperationType.QUERY;
        StringBuilder description = new StringBuilder();

        description.append("可取余额/总余额：" + desirableBalance_balance + "元");

        AccountLog accountLog = new AccountLog(userId,cardId,employee,operationType,description.toString());
        accountLogRepository.save(accountLog);

        logger.info(accountLog.toString());
    }
}
