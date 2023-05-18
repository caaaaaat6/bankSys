package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountLogRepository extends CrudRepository<AccountLog, Long> {

    Optional<AccountLog> findFirstByOrderByLogIdDesc();

    List<AccountLog> findAllByEmployee(Employee employee);

//    List<AccountLog> findAllByEmployeeId(Employee employee);

    List<AccountLog> findAllByEmployeeIn(List<Employee> employees);

    List<AccountLog> findAllByCardIdAndOperationTypeOrderByDateDesc(long cardId, String operationType);
}
