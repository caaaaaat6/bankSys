package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Enterprise;
import org.springframework.data.repository.CrudRepository;

public interface EnterpriseRepository extends CrudRepository<Enterprise, Long> {
}
