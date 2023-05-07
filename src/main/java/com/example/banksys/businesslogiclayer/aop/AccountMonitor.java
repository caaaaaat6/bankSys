package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.BaseAccount;
import com.example.banksys.businesslogiclayer.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.BaseFixedAccountRight;
import com.example.banksys.businesslogiclayer.EnterpriseUserAccount;
import com.example.banksys.businesslogiclayer.exception.EnterpriseAccountOpenMoneyNotEnoughException;
import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.log.AccountLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class AccountMonitor {

    private static final Logger logger = LoggerFactory.getLogger(AccountMonitor.class);

    private static final double ENTERPRISE_OPEN_MONEY_THRESHOLD = 10000;

    @Autowired
    @Lazy
    private AccountLogRepository accountLogRepository;

//    @Autowired
//    @Lazy
//    private CardRepository cardRepository;

    @Autowired
    @Lazy
    private UserRepository userRepository;

//    @Before("execution(* com.example.banksys.businesslogiclayer.EnterpriseUserAccount+.openEnterpriseAccount(..))" +
//            " && args(userId, userPid, userName, userType, password, enterpriseId, cardType, openMoney)")
//    @Before("execution(* com.example.banksys.businesslogiclayer.EnterpriseCurrentUserAccount.openEnterpriseAccount(..))"
//            +
//            " && args(userId, userPid, userName, userType, password, enterpriseId, cardType, openMoney)"
//    )
//    @Before("execution(public * openEnterpriseAccount(..))")
    @Before("execution(public * *(..))")
//    public void beforeEnterpriseOpenAccount(JoinPoint joinPoint, long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney) throws EnterpriseAccountOpenMoneyNotEnoughException {
    public void beforeEnterpriseOpenAccount(JoinPoint joinPoint) throws EnterpriseAccountOpenMoneyNotEnoughException {
        // 开户金额是否大于等于1万，否则抛错
//        if (openMoney < ENTERPRISE_OPEN_MONEY_THRESHOLD) {
//            throw new EnterpriseAccountOpenMoneyNotEnoughException("开户金额不足" + ENTERPRISE_OPEN_MONEY_THRESHOLD);
//        }
//        EnterpriseUserAccount enterpriseUserAccount = (EnterpriseUserAccount) joinPoint.getTarget();
//        EnterpriseUser enterpriseUser = enterpriseUserAccount.getEnterpriseUser();
//        Enterprise enterprise = enterpriseUser.getEnterprise();
//
//        // 确定企业用户的权限，第一人为SUPER，否则为USER
//        if (enterprise.getEnterpriseUserList() == null || enterprise.getEnterpriseUserList().size() == 0) {
//            enterpriseUser.setRight(EnterpriseUser.Right.SUPER);
//        } else {
//            enterpriseUser.setRight(EnterpriseUser.Right.USER);
//        }
//        userRepository.save(enterpriseUser);
    }

    @Around("execution(* com.example.banksys.businesslogiclayer.BaseCurrentAccountRight+.deposit*(..))" +
            " || execution(* com.example.banksys.businesslogiclayer.BaseFixedAccountRight+.deposit*(..))")
    public Object aroundDeposit(ProceedingJoinPoint joinPoint) throws Throwable {
        BaseAccount account = (BaseAccount) joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        double money = (double) args[0];

        // 获取accountLog所需的信息
        Card card = account.getCard();
        long userId = card.getUserId();
        long cardId = card.getCardId();
        Long employeeId = account.getEmployeeId();
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
        }
        description.append("存前余额：" + card.getBalance() + "元\n");

        // 执行
        Object obj = joinPoint.proceed();

        description.append("存后余额：" + card.getBalance() + "元");

        // 保存日志
        AccountLog log = new AccountLog(userId, cardId, employeeId, operationType, description.toString());
        accountLogRepository.save(log);

        // 后台输出日志
        logger.info("[around]: ---> " + log.toString());

        return obj;
    }
}
