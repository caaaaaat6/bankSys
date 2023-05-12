package com.example.banksys.model;

import com.example.banksys.businesslogiclayer.employeeaccount.EmployeeRight;
import com.example.banksys.model.log.AccountLog;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Employee")
//@DiscriminatorColumn(name = "employeeType", discriminatorType = DiscriminatorType.STRING, length = 30)
//@DiscriminatorValue(Employee.EmployeeType.FRONT_DESK)
@Data
@NoArgsConstructor(force = true)
public class Employee implements EmployeeRight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long employeeId;

    @Column(length = 16, nullable = false)
    private String employeeType;

    @Column(length = 32, nullable = false)
    private String employeeName;

    @Column(length = 512, nullable = false)
    private String password;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Department department;

    public Employee(String employeeType, String employeeName, String password) {
        this.employeeType = employeeType;
        this.employeeName = employeeName;
        this.password = password;
    }

    public static class EmployeeType {
        public static final String FRONT_DESK = "前台操作员";
        public static final String MANAGER = "银行经理";
        public static final String HEAD = "银行业务总管";
    }

    @Override
    public void changePassword() {

    }

    @Override
    public List<AccountLog> findReport() {
        return null;
    }
}
