package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Card;
import com.example.banksys.model.Trade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TradeRepository extends CrudRepository<Trade, Long> {

    @Query(value = "select coalesce(sum(t.money), 0) from Trade as t where t.cardId = :#{#card.cardId} and current_timestamp < t.expireDate and t.tradeType = 'fixed' ")
    Double findFixedBalance(Card card);

    List<Trade> findAllByCardIdOrderByTradeDateDesc(long cardId);
}
