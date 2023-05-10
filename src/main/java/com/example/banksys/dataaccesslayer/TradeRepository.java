package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Trade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TradeRepository extends CrudRepository<Trade, Long> {

    @Query(value = "select coalesce(sum(t.money), 0) from Trade as t where current_timestamp < t.expireDate and t.tradeType = 'fixed' ")
    Double findFixedBalance();
}
