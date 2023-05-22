package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;

import java.util.List;

/**
 * 雇员权力接口
 */
public interface EmployeeRight {

    /**
     * 查询所管雇员的流水
     * @return 所管雇员的日志
     */
    List<AccountLog> findReport();

    /**
     * 查询所管的雇员
     * @return 所管的雇员，默认为null，因为前台操作员没有管理其他雇员
     */
    default List<Employee> findEmployeeManaged() {
        return null;
    }
}
