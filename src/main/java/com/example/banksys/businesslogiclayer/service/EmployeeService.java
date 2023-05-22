package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.employeeaccount.BaseEmployeeAccount;
import com.example.banksys.dataaccesslayer.DepartmentRepository;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.form.AddDepartmentForm;
import com.example.banksys.presentationlayer.form.RegisterForm;
import com.example.banksys.presentationlayer.form.ReviewForm;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * 雇员Service
 */
public interface EmployeeService {

    /**
     * 雇员注册
     * @param employeeRepository
     * @param departmentRepository
     * @param encoder
     * @param form 注册表单
     * @return 所新开雇员账户的ID
     */
    Long register(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, PasswordEncoder encoder, RegisterForm form);

    /**
     * 查询自己或所管雇员的流水
     * @param account
     * @return 自己或所管雇员的流水
     */
    List<AccountLog> findReport(BaseEmployeeAccount account);

    /**
     * 查询所管的雇员
     * @return 所管的雇员
     */
    List<Employee> findEmployeeManaged(BaseEmployeeAccount account);

    /**
     * 供系统管理员使用的，查询还没有通过审核的雇员
     * @param employeeRepository
     * @return
     */
    List<Employee> findEmployeeNotEnabled(EmployeeRepository employeeRepository);

    /**
     * 系统管理员审核雇员
     * @param employeeRepository
     * @param form 审核表单
     */
    void reviewEmployee(EmployeeRepository employeeRepository, ReviewForm form);

    /**
     * 添加部门
     * @param departmentRepository
     * @param form 添加部门表单
     */
    void addDepartment(DepartmentRepository departmentRepository, AddDepartmentForm form);
}
