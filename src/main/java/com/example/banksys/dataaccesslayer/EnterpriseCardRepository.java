package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Card;
import com.example.banksys.model.EnterpriseCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EnterpriseCardRepository extends CrudRepository<EnterpriseCard, Long> {

//    @Query("select c from EnterpriseCard c where c.userPid = ?1 and c. = 'PersonalCard'")
    List<EnterpriseCard> findEnterpriseCardsByUserPid(String userPid);
}
