package com.example.banksys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Employee")
@Data
@NoArgsConstructor(force = true)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long employeeId;

    @Column(length = 16, nullable = false)
    private String employeeType;

    @Column(length = 32, nullable = false)
    private String employeeName;

    @Column(length = 512, nullable = false)
    private String password;

    public Employee(String employeeType, String employeeName, String password) {
        this.employeeType = employeeType;
        this.employeeName = employeeName;
        this.password = password;
    }
}
