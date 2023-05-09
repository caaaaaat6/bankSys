package com.example.banksys.businesslogiclayer.aop;

import com.example.banksys.businesslogiclayer.BaseAccount;
import jakarta.persistence.PersistenceContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WithdrawMonitor {

    private static final Logger logger = LoggerFactory.getLogger(WithdrawMonitor.class);

    @Pointcut(value = "execution(* com.example.banksys.businesslogiclayer.BaseAccount+.withdraw(..))")
    public void withdraw() {

    }

//    @Before(value = "execution(* withdraw(..)) && args(money)")
//    public void beforeWithdraw(JoinPoint joinPoint, double money) {
//        BaseAccount account = (BaseAccount) joinPoint.getTarget();
//        double balance = account.getCard().getBalance();
//
//
//
//        logger.info("-----------------weave success-----------------");
//    }
}
