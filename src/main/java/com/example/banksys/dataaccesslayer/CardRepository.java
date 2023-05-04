package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Card;
import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, Long> {
}
