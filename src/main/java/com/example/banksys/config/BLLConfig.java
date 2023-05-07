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

//    @Bean
//    public OrdinaryCurrentUserAccount getOrdinaryCurrentUserAccount() {
//        return new OrdinaryCurrentUserAccount();
//    }
//
//    @Bean
//    public OrdinaryFixedUserAccount getOrdinaryFixedUserAccount() {
//        return new OrdinaryFixedUserAccount();
//    }
//
//    @Bean
//    public EnterpriseCurrentUserAccount getEnterpriseCurrentUserAccount() {
//        return new EnterpriseCurrentUserAccount();
//    }
//
//    @Bean
//    public EnterpriseFixedUserAccount getEnterpriseFixedUserAccount() {
//        return new EnterpriseFixedUserAccount();
//    }
//
//    @Bean
//    public VIPFixedUserAccount getVIPFixedUserAccount() {
//        return new VIPFixedUserAccount();
//    }
//
//    @Bean
//    public VIPCurrentUserAccount getVIPCurrentUserAccount() {
//        return new VIPCurrentUserAccount();
//    }
}
