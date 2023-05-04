package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
