package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.employeeaccount.BaseEmployeeAccount;
import com.example.banksys.dataaccesslayer.DepartmentRepository;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.model.Department;
import com.example.banksys.model.Employee;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.form.AddDepartmentForm;
import com.example.banksys.presentationlayer.form.RegisterForm;
import com.example.banksys.presentationlayer.form.ReviewForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public Long register(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, PasswordEncoder encoder, RegisterForm form) {
        Department department = departmentRepository.findById(form.getDepartmentId()).get();
        Employee employee = new Employee(form.getEmployeeType(), form.getUserName(), form.getUserPid(),department, encoder.encode(form.getPassword()));
        if (form.getEmployeeType().equals(Employee.EmployeeType.ADMIN_EN)) {
            employee.setEnabled(true);
        }
        Employee save = employeeRepository.save(employee);

        if (!form.getEmployeeType().equals(Employee.EmployeeType.ADMIN_EN)
            || !form.getEmployeeType().equals(Employee.EmployeeType.FIXED_HEAD_EN)
            || !form.getEmployeeType().equals(Employee.EmployeeType.CURRENT_HEAD_EN)) {
            department.getEmployeeList().add(save);
        }
        departmentRepository.save(department); //同时更新部门的列表，因为用了OneToMany，需要更新

        save.setEmployeeId(save.getUserId());
        employeeRepository.save(save);
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

    @Override
    public List<Employee> findEmployeeNotEnabled(EmployeeRepository employeeRepository) {
        return employeeRepository.findAllByEnabled(false);
    }

    // 已勾选审核雇员表单中的所有雇员的enable都设置为true，即已审核通过
    @Override
    public void reviewEmployee(EmployeeRepository employeeRepository, ReviewForm form) {
        Iterable<Employee> allById = employeeRepository.findAllById(form.getSelectedEmployeesId());
        allById.forEach(employee -> employee.setEnabled(true));
        employeeRepository.saveAll(allById);
    }

    @Override
    public void addDepartment(DepartmentRepository departmentRepository, AddDepartmentForm form) {
        Department department = new Department(form.getDepartmentName());
        departmentRepository.save(department);
    }

}
