package com.example.banksys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Employee> employeeList;
}
