package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.BaseAccount;
import com.example.banksys.businesslogiclayer.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.BaseFixedAccountRight;
import com.example.banksys.businesslogiclayer.EnterpriseUserAccount;
import com.example.banksys.businesslogiclayer.exception.EnterpriseAccountOpenMoneyNotEnoughException;
import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.dataaccesslayer.*;
import com.example.banksys.model.Card;
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
public class AccountMonitor {

    private static final Logger logger = LoggerFactory.getLogger(AccountMonitor.class);

    private static final double ENTERPRISE_OPEN_MONEY_THRESHOLD = 10000;

    @Autowired
    private AccountLogRepository accountLogRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private EnterpriseCardRepository enterpriseCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnterpriseUserRepository enterpriseUserRepository;

    @Before("execution(* com.example.banksys.businesslogiclayer.PersonalUserAccount+.openAccount(..))")
    public void beforePersonalOpenAccount(JoinPoint joinPoint) {
        logger.info("---------------------------------------weave success----------------------------------------");
    }

    @Before(value = "execution(* com.example.banksys.businesslogiclayer.EnterpriseUserAccount+.openEnterpriseAccount(..))" +
            " && args(userId, userPid, userName, password, enterpriseId, cardType, openMoney)")
    public void beforeEnterpriseOpenAccount(JoinPoint joinPoint, long userId, String userPid, String userName, String password, Long enterpriseId, String cardType, double openMoney) throws EnterpriseAccountOpenMoneyNotEnoughException {
        // 开户金额是否大于等于1万，否则抛错
        if (openMoney < ENTERPRISE_OPEN_MONEY_THRESHOLD) {
            throw new EnterpriseAccountOpenMoneyNotEnoughException("开户金额不足" + ENTERPRISE_OPEN_MONEY_THRESHOLD);
        }

        EnterpriseUserAccount enterpriseUserAccount = (EnterpriseUserAccount) joinPoint.getTarget();

        Enterprise enterprise = enterpriseUserAccount.getEnterprise();
        // 这个enterpriseUser是当前账户的操作人
        EnterpriseUser enterpriseUser  = enterpriseUserAccount.getEnterpriseUser();

        // 确定企业用户的权限，第一人为SUPER，否则为USER
        if (enterprise.getEnterpriseUserList() == null || enterprise.getEnterpriseUserList().size() == 0) {
            enterpriseUser.setRightType(EnterpriseUser.RightType.SUPER);
        } else {
            enterpriseUser.setRightType(EnterpriseUser.RightType.USER);
        }
        userRepository.save(enterpriseUser);

        // 如果企业有账户（卡）就不开
//        enterpriseCardRepository
    }

    @AfterReturning(value = "execution(* com.example.banksys.businesslogiclayer.EnterpriseUserAccount+.openEnterpriseAccount(..))" +
            " && args(userId, userPid, userName,password, enterpriseId, cardType, openMoney, employeeId)", returning = "cardId")
    public void afterReturningOpen(JoinPoint joinPoint,long userId, String userPid, String userName, String password, Long enterpriseId, String cardType, double openMoney, Long cardId, Long employeeId) {
        String operationType = AccountLog.OperationType.OPEN;
        StringBuilder description = new StringBuilder();

        description.append("开户金额：" + openMoney + "元\n");
        description.append("账户类型：" + Card.UserType.ENTERPRISE + "\n");
        description.append("定期活期：" + cardType + "\n");

        AccountLog log = new AccountLog(userId, cardId, employeeId, operationType, description.toString());
        accountLogRepository.save(log);

        // 后台输出日志
        logger.info("[around]: ---> " + log.toString());
    }

    // 没用
    public EnterpriseUser createEnterpriseUser(EnterpriseUserAccount enterpriseUserAccount, long userId, String userPid, String userName, String userType, String password) {
        Optional<EnterpriseUser> byId = enterpriseUserRepository.findById(userId);
        if (byId.isPresent()) {
            return byId.get();
        }
        EnterpriseUser enterpriseUser = new EnterpriseUser();
        enterpriseUser.setUserId(userId);
        enterpriseUser.setUserPid(userPid);
        enterpriseUser.setUserName(userName);
        enterpriseUser.setUserType(userType);
        enterpriseUser.setPassword(password);
        enterpriseUser.setEnterprise(enterpriseUserAccount.getEnterprise());
        enterpriseUser.setEnterpriseCard(enterpriseUserAccount.getEnterpriseCard());

        enterpriseUserAccount.setEnterpriseUser(enterpriseUser);
        return enterpriseUser;
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
            description.append("到期时间：" + expireDate + "\n");
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
