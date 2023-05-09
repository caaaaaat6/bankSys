package com.example.banksys.config;

import com.example.banksys.businesslogiclayer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;

import java.util.Date;

@Configuration
@EnableAspectJAutoProxy
public class BLLConfig {

    @Bean
    @Scope(value = "prototype")
    public Date getDate() {
        return new Date();
    }

    @Bean
    @Scope(value = "prototype")
    public OrdinaryCurrentUserAccount getOrdinaryCurrentUserAccount() {
        return new OrdinaryCurrentUserAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public OrdinaryFixedUserAccount getOrdinaryFixedUserAccount() {
        return new OrdinaryFixedUserAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public EnterpriseCurrentUserAccount getEnterpriseCurrentUserAccount() {
        return new EnterpriseCurrentUserAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public EnterpriseFixedUserAccount getEnterpriseFixedUserAccount() {
        return new EnterpriseFixedUserAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public VIPFixedUserAccount getVIPFixedUserAccount() {
        return new VIPFixedUserAccount();
    }

    @Bean
    @Scope(value = "prototype")
    public VIPCurrentUserAccount getVIPCurrentUserAccount() {
        return new VIPCurrentUserAccount();
    }
}
