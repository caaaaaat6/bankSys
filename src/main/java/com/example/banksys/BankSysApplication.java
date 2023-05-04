package com.example.banksys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankSysApplication {

//    @Bean
//    private TradeRepository tradeRepository;

    public static void main(String[] args) {
        SpringApplication.run(BankSysApplication.class, args);
//         tradeRepository;
//        Iterable<Trade> all = tradeRepository.findAll();
//        all.forEach(System.out::println);
    }

}
