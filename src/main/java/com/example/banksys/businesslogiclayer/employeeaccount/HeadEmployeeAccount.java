package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 银行总管账号基类
 */
@Data
public abstract class HeadEmployeeAccount extends ManagerEmployeeAccount {

    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * 查询银行内所有雇员
     * @return 银行内所有雇员
     */
    @Override
    public List<Employee> findEmployeeManaged() {
        return employeeRepository.findAll();
    }
}
