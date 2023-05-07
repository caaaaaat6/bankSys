package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.EnterpriseUser;
import org.springframework.data.repository.CrudRepository;

public interface EnterpriseUserRepository extends CrudRepository<EnterpriseUser, Long> {
}
