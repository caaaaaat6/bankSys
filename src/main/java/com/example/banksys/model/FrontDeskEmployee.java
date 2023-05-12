package com.example.banksys.model;

import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.model.log.AccountLog;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Data
@NoArgsConstructor(force = true)
public class FrontDeskEmployee extends Employee {

    @Transient
    @Autowired
    AccountLogRepository accountLogRepository;

    @Override
    public List<AccountLog> findReport() {
        return accountLogRepository.findAllByEmployeeId(getEmployeeId());
    }
}
