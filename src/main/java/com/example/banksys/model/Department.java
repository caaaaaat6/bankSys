package com.example.banksys.model;

import com.example.banksys.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 部门模型
 */
@Entity
@Table(name = "Department")
@Data
@NoArgsConstructor(force = true)
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    private String departmentName;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Employee> employeeList;

    public Department(String departmentName) {
        this.departmentName = departmentName;
    }
}
