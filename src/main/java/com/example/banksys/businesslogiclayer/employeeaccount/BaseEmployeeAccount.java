package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.model.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 所有雇员账号的基类
 */
@Data
@NoArgsConstructor(force = true)
public abstract class BaseEmployeeAccount implements EmployeeRight {

    /**
     * 一个雇员账号持有一个雇员
     */
    private Employee employee;

}
