package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.BaseAccount;
import com.example.banksys.businesslogiclayer.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.BaseFixedAccountRight;
import com.example.banksys.businesslogiclayer.EnterpriseUserAccount;
import com.example.banksys.businesslogiclayer.exception.EnterpriseAccountOpenMoneyNotEnoughException;
import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.dataaccesslayer.*;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.log.AccountLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Aspect
@Component
public class DepositMonitor {

    private static final Logger logger = LoggerFactory.getLogger(DepositMonitor.class);

    @Autowired
    private AccountLogRepository accountLogRepository;

    @Around("execution(* com.example.banksys.businesslogiclayer.BaseCurrentAccountRight+.deposit*(..))" +
            " || execution(* com.example.banksys.businesslogiclayer.BaseFixedAccountRight+.deposit*(..)) ")
    public Object aroundDeposit(ProceedingJoinPoint joinPoint) throws Throwable {
        BaseAccount account = (BaseAccount) joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        double money = (double) args[0];

        // 获取accountLog所需的信息
        Card card = account.getCard();
        long userId = card.getUserId();
        long cardId = card.getCardId();
        Employee employee = account.getEmployee();
        String operationType = "";
        StringBuilder description = new StringBuilder();

        // 处理log中的description
        description.append("存款金额：" + money + "元\n");
        if (account instanceof BaseCurrentAccountRight) {
            operationType = AccountLog.OperationType.CURRENT_DEPOSIT;
        } else if (account instanceof BaseFixedAccountRight) {
            operationType = AccountLog.OperationType.FIXED_DEPOSIT;
            int depositDays = (int) args[1];
            Date expireDate = BLLUtil.getExpireDate(depositDays);
            description.append("定期时间：" + depositDays + "天\n");
            description.append("到期时间：" + expireDate + "\n");
        }
        description.append("存前余额：" + card.getBalance() + "元\n");

        // 执行
        Object obj = joinPoint.proceed();

        description.append("存后余额：" + card.getBalance() + "元");

        // 保存日志
        AccountLog log = new AccountLog(userId, cardId, employee, operationType, description.toString());
        accountLogRepository.save(log);

        // 后台输出日志
        logger.info("[around]: ---> " + log.toString());

        return obj;
    }
}
