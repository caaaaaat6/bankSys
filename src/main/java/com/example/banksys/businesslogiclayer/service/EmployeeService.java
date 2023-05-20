package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.employeeaccount.BaseEmployeeAccount;
import com.example.banksys.dataaccesslayer.DepartmentRepository;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.form.RegisterForm;
import com.example.banksys.presentationlayer.form.ReviewForm;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public interface EmployeeService {


    Long register(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, PasswordEncoder encoder, RegisterForm form);

    List<AccountLog> findReport(BaseEmployeeAccount account);

    List<Employee> findEmployeeManaged(BaseEmployeeAccount account);

    List<Employee> findEmployeeNotEnabled(EmployeeRepository employeeRepository);

    void reviewEmployee(EmployeeRepository employeeRepository, ReviewForm form);
}
