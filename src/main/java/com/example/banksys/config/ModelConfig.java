package com.example.banksys.config;

import com.example.banksys.businesslogiclayer.employeeaccount.FrontDeskEmployeeAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ModelConfig {

    @Bean
    @Scope("prototype")
    public FrontDeskEmployeeAccount getFrontDestEmployee() {
        return new FrontDeskEmployeeAccount();
    }
}
