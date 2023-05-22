package com.example.banksys.config;

import com.example.banksys.businesslogiclayer.employeeaccount.CurrentHeadEmployeeAccount;
import com.example.banksys.businesslogiclayer.employeeaccount.FixedHeadEmployeeAccount;
import com.example.banksys.businesslogiclayer.employeeaccount.FrontDeskEmployeeAccount;
import com.example.banksys.businesslogiclayer.employeeaccount.ManagerEmployeeAccount;
import com.example.banksys.businesslogiclayer.useraccount.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;

import java.util.Date;

/**
 * Bean配置类
 */
@Configuration
@EnableAspectJAutoProxy
public class BLLConfig {

    @Bean
    @Scope(value = "prototype")
    public Date getDate() {
        return new Date();
    }

    @Bean
    @Scope(value = "session")
    public OrdinaryCurrentUserAccount ordinaryCurrentUserAccount() {
        return new OrdinaryCurrentUserAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public OrdinaryFixedUserAccount ordinaryFixedUserAccount() {
        return new OrdinaryFixedUserAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public EnterpriseCurrentUserAccount enterpriseCurrentUserAccount() {
        return new EnterpriseCurrentUserAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public EnterpriseFixedUserAccount enterpriseFixedUserAccount() {
        return new EnterpriseFixedUserAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public VIPFixedUserAccount vipFixedUserAccount() {
        return new VIPFixedUserAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public VIPCurrentUserAccount vipCurrentUserAccount() {
        return new VIPCurrentUserAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public CurrentHeadEmployeeAccount currentHeadEmployeeAccount() {
        return new CurrentHeadEmployeeAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public FixedHeadEmployeeAccount fixedHeadEmployeeAccount() {
        return new FixedHeadEmployeeAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public ManagerEmployeeAccount managerEmployeeAccount() {
        return new ManagerEmployeeAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public FrontDeskEmployeeAccount frontDeskEmployeeAccount() {
        return new FrontDeskEmployeeAccount();
    }

}
