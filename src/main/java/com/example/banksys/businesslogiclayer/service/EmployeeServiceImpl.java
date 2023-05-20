package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.employeeaccount.BaseEmployeeAccount;
import com.example.banksys.dataaccesslayer.DepartmentRepository;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.model.Department;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.form.RegisterForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Override
    public Long register(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, PasswordEncoder encoder, RegisterForm form) {
        Department department = departmentRepository.findById(form.getDepartmentId()).get();
        Employee employee = new Employee(form.getEmployeeType(), form.getUserName(), form.getUserPid(),department, encoder.encode(form.getPassword()));
        Employee save = employeeRepository.save(employee);
        return save.getUserId();
    }

    @Override
    public List<AccountLog> findReport(BaseEmployeeAccount account) {
        return account.findReport();
    }

    @Override
    public List<Employee> findEmployeeManaged(BaseEmployeeAccount account) {
        return account.findEmployeeManaged();
    }
}
