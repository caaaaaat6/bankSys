package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.EnterpriseCard;
import org.springframework.data.repository.CrudRepository;

public interface EnterpriseCardRepository extends CrudRepository<EnterpriseCard, Long> {
}
