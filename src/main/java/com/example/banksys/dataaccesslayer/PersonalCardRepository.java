package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PersonalCardRepository extends CrudRepository<PersonalCard, Long> {

    Optional<List<PersonalCard>> findPersonalCardsByUserPid(String userPid);
}
