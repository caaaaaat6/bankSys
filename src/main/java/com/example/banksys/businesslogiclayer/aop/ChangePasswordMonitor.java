package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 改密码的切面，负责记录改密码日志
 */
@Slf4j
@Aspect
@Component
public class ChangePasswordMonitor {
    private AccountLogRepository accountLogRepository;

    public ChangePasswordMonitor(AccountLogRepository accountLogRepository) {
        this.accountLogRepository = accountLogRepository;
    }

    /**
     * 产生改密码的日志，日志内容：”修改账户密码“， 并在后台控制台上产生一条log
     * @param joinPoint
     */
    @After(value = "execution(* com.example.banksys.businesslogiclayer.useraccount.BaseAccount+.changePassword(..))")
    public void changePasswordLog(JoinPoint joinPoint) {
        BaseAccount account = (BaseAccount) joinPoint.getTarget();

        // 获取accountLog所需的信息
        Card card = account.getCard();
        long userId = card.getUserId();
        long cardId = card.getCardId();
        Employee employee = account.getEmployee();
        String operationType = AccountLog.OperationType.CHANGE_PASSWORD;
        StringBuilder description = new StringBuilder();

        description.append("修改账户密码");

        AccountLog accountLog = new AccountLog(userId,cardId,employee,operationType,description.toString());
        accountLogRepository.save(accountLog);

        log.info(accountLog.toString());
    }
}
