package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

}
