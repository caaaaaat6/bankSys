package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
public abstract class HeadEmployeeAccount extends ManagerEmployeeAccount {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findEmployeeManaged() {
        return employeeRepository.findAll();
    }
}
