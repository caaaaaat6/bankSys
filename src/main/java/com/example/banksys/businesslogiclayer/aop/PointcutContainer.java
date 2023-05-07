package com.example.banksys.businesslogiclayer.aop;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

public class PointcutContainer {

//    @Pointcut("execution(* com.example.banksys.businesslogiclayer.*Account+.deposit*(..))")
    @Pointcut("execution(* com.example.banksys.businesslogiclayer.BaseCurrentAccountRight+.deposit*(..))")
    public void deposit() {}
}
