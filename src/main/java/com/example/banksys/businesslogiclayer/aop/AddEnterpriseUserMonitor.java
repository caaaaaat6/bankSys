package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.businesslogiclayer.useraccount.EnterpriseUserAccount;
import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AddEnterpriseUserMonitor {

    private AccountLogRepository accountLogRepository;

    public AddEnterpriseUserMonitor(AccountLogRepository accountLogRepository) {
        this.accountLogRepository = accountLogRepository;
    }

    @AfterReturning(value = "execution(* com.example.banksys.businesslogiclayer.service.EnterpriseService+.addEnterpriseUser(..))", returning = "userId")
    public void addEnterpriseUserLog(JoinPoint joinPoint, Long userId) {
        Object[] args = joinPoint.getArgs();
        EnterpriseUserAccount account = (EnterpriseUserAccount) args[0];

        // 获取accountLog所需的信息
        Card card = account.getCard();
        long cardId = card.getCardId();
        Employee employee = account.getEmployee();
        String operationType = AccountLog.OperationType.OPEN;
        StringBuilder description = new StringBuilder();

        description.append("由该企业super用户创建\n");
        description.append("账户类型：" + card.getUserType() + "\n");
        description.append("定期活期：" + card.getCardType() + "\n");
        AccountLog accountLog = new AccountLog(userId,cardId,employee,operationType,description.toString());
        accountLogRepository.save(accountLog);

        log.info(accountLog.toString());
    }
}
