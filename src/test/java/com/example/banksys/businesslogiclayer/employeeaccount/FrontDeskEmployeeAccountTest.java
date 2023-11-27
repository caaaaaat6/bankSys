package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.businesslogiclayer.employeeaccount.FrontDeskEmployeeAccount;
import com.example.banksys.dataaccesslayer.DepartmentRepository;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class FrontDeskEmployeeAccountTest {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Qualifier("getFrontDeskEmployeeAccount")
    @Autowired
    FrontDeskEmployeeAccount frontDeskEmployeeAccount;


    @Test
    void findReport() {
        Employee employee = employeeRepository.findById(2L).get();
        frontDeskEmployeeAccount.setEmployee(employee);
        List<AccountLog> report = frontDeskEmployeeAccount.findReport();

        Deque<Integer> que = new LinkedList<>();
        Map<Integer, Integer> map = new HashMap<>();
        assert report.size() == 1;
    }
}