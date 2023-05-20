package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Card;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CardRepository extends CrudRepository<Card, Long> {

//    @Query("select c from Card c where c.userPid = ?1 and c. = 'PersonalCard'")
    List<Card> findCardsByUserPid(String userPid);

    List<Card> findCardsByCardType(String cardType);
}
