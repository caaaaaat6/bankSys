package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 前台操作员账号
 */
@Data
@NoArgsConstructor(force = true)
public class FrontDeskEmployeeAccount extends BaseEmployeeAccount {

    @Transient
    @Autowired
    AccountLogRepository accountLogRepository;

    /**
     * 查看自己的流水报告
     * @return 返回自己的流水报告
     */
    @Override
    public List<AccountLog> findReport() {
        return accountLogRepository.findAllByEmployee(getEmployee());
    }
}
