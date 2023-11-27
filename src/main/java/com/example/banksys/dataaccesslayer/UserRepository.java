package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Card;
import com.example.banksys.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User /* 实体类 */, Long /* 主键类型 */> {

    User findByCard(Card card);

    User findByUserIdAndPassword(Long userId, String password);
}
