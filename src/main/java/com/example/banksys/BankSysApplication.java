package com.example.banksys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/*
   @SpringBootApplication包含的注解有：
   1.@Configuration 配置Bean
   2.@EnableAutoConfiguration 启用Spring Boot的自动配置机制，它会自动添加所需的依赖项和配置，以使应用程序能够运行
   3.@ComponentScan 指示Spring Boot扫描当前包及其子包中的所有@Component、@Service、@Repository和@Controller注解的类，
    并将它们注册为Spring Bean
 */
@SpringBootApplication
//@EnableJpaRepositories("com.example.banksys.dataaccesslayer")
public class BankSysApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankSysApplication.class, args);
    }

}
