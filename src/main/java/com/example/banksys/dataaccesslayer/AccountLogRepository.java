package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.log.AccountLog;
import org.springframework.data.repository.CrudRepository;

public interface AccountLogRepository extends CrudRepository<AccountLog, Long> {
}
