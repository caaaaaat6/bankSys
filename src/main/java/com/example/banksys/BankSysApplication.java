package com.example.banksys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories("com.example.banksys.dataaccesslayer")
public class BankSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankSysApplication.class, args);
    }

}
