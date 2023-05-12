package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;

import java.util.List;

public interface EmployeeRight {

    void changePassword();

    List<AccountLog> findReport();

    default List<Employee> findEmployeeManaged() {
        return null;
    }
}
