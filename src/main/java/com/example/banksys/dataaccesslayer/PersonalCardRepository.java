package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.PersonalCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonalCardRepository extends CrudRepository<PersonalCard, Long> {

    List<PersonalCard> findPersonalCardsByUserPid(String userPid);
}
