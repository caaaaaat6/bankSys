package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.EnterpriseUserAccount;
import com.example.banksys.businesslogiclayer.exception.EnterpriseAccountOpenMoneyNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.EnterpriseCardExsitException;
import com.example.banksys.businesslogiclayer.exception.FiveEnterpriseAccountOpenedException;
import com.example.banksys.dataaccesslayer.*;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.log.AccountLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OpenAccountMonitor {

    private static final Logger logger = LoggerFactory.getLogger(OpenAccountMonitor.class);

    private static final double ENTERPRISE_OPEN_MONEY_THRESHOLD = 10000;

    @Autowired
    private AccountLogRepository accountLogRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 个人用户开户后的日志切面
     * @param joinPoint
     * @param employee
     */
    @AfterReturning(value = "execution(* com.example.banksys.businesslogiclayer.PersonalUserAccount+.openAccount(..)) " +
            " && args(userId, userPid, userName, userType, password, enterpriseId, cardType, openMoney, employee)", returning = "cardId")
    public void afterReturningPersonalOpenAccountLog(JoinPoint joinPoint, long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney, Employee employee, Long cardId) {
        String operationType = AccountLog.OperationType.OPEN;
        StringBuilder description = new StringBuilder();

        description.append("开户金额：" + openMoney + "元\n");
        description.append("账户类型：" + userType + "\n");
        description.append("定期活期：" + cardType + "\n");

        AccountLog log = new AccountLog(userId, cardId, employee, operationType, description.toString());
        accountLogRepository.save(log);

        // 后台输出日志
        logger.info("[afterReturning]: ---> " + log.toString());
    }

    /**
     * 企业用户开户前的切面
     * @throws EnterpriseAccountOpenMoneyNotEnoughException
     */
    @Before(value = "execution(* com.example.banksys.businesslogiclayer.EnterpriseUserAccount+.openEnterpriseAccount(..))" +
            " && args(userId, userPid, userName, password, enterpriseId, cardType, openMoney, employeeId)")
    public void beforeEnterpriseOpenAccount(JoinPoint joinPoint, long userId, String userPid, String userName, String password, Long enterpriseId, String cardType, double openMoney, Long employeeId) throws EnterpriseAccountOpenMoneyNotEnoughException, FiveEnterpriseAccountOpenedException, EnterpriseCardExsitException {
        EnterpriseUserAccount enterpriseUserAccount = (EnterpriseUserAccount) joinPoint.getTarget();
        Enterprise enterprise = enterpriseUserAccount.getEnterprise();

        // 检查该企业是否已开够五个户，若是，则抛错
        if (enterprise.getEnterpriseUserList().size() >= 5) {
            throw new FiveEnterpriseAccountOpenedException("该企业已经开够5个账户，无法继续开户！");
        }

        // 开户金额是否大于等于1万，否则抛错
        if (openMoney < ENTERPRISE_OPEN_MONEY_THRESHOLD) {
            throw new EnterpriseAccountOpenMoneyNotEnoughException("开户金额不足" + ENTERPRISE_OPEN_MONEY_THRESHOLD);
        }

        // 这个enterpriseUser是当前账户的操作人
        EnterpriseUser enterpriseUser  = enterpriseUserAccount.getEnterpriseUser();

        // 确定企业用户的权限，第一人为SUPER，否则为USER
        if (enterprise.getEnterpriseUserList() == null || enterprise.getEnterpriseUserList().size() == 0) {
            enterpriseUser.setRightType(EnterpriseUser.RightType.SUPER);
        } else {
            enterpriseUser.setRightType(EnterpriseUser.RightType.USER);
            throw new EnterpriseCardExsitException("该企业已经开了一个账户，无法开更多的账户！");
        }
        userRepository.save(enterpriseUser);
    }

    /**
     *  企业用户开户日志
     */
    @AfterReturning(value = "execution(* com.example.banksys.businesslogiclayer.EnterpriseUserAccount+.openEnterpriseAccount(..))" +
            " && args(userId, userPid, userName, password, enterpriseId, cardType, openMoney, employee)", returning = "cardId")
    public void afterReturningOpen(JoinPoint joinPoint, long userId, String userPid, String userName, String password, Long enterpriseId, String cardType, double openMoney, Long cardId, Employee employee) {
        String operationType = AccountLog.OperationType.OPEN;
        StringBuilder description = new StringBuilder();

        description.append("开户金额：" + openMoney + "元\n");
        description.append("账户类型：" + Card.UserType.ENTERPRISE + "\n");
        description.append("定期活期：" + cardType + "\n");

        AccountLog log = new AccountLog(userId, cardId, employee, operationType, description.toString());
        accountLogRepository.save(log);

        // 后台输出日志
        logger.info("[afterReturning]: ---> " + log.toString());
    }
}
