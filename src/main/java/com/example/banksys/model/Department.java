package com.example.banksys.model;

import com.example.banksys.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
}
