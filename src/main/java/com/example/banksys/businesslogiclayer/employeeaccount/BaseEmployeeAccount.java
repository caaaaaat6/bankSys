package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.model.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor(force = true)
public abstract class BaseEmployeeAccount implements EmployeeRight {

    private Employee employee;

}
