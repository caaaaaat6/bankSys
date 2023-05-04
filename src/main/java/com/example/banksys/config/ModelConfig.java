package com.example.banksys.config;

import com.example.banksys.model.BaseAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {

    @Bean
    public BaseAccount baseEmployeeAccount() {
        return new BaseAccount();
    }
}
