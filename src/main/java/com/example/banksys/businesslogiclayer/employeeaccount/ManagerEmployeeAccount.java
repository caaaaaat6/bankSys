package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class ManagerEmployeeAccount extends FrontDeskEmployeeAccount {

    @Override
    public List<AccountLog> findReport() {
        return getAccountLogRepository().findAllByEmployeeIn(getEmployee().getDepartment().getEmployeeList());
    }

    @Override
    public List<Employee> findEmployeeManaged() {
        return getEmployee().getDepartment().getEmployeeList();
    }
}
