package com.example.banksys.model;

import com.example.banksys.model.log.AccountLog;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor(force = true)
public class Manager extends FrontDeskEmployee {

//    @Override
//    public List<AccountLog> findReport() {
//        return getAccountLogRepository().findAllByEmployeeId(getDepartment().getEmployeeList()).get();
//    }
}
