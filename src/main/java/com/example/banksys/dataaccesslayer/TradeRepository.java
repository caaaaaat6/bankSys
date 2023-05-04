package com.example.banksys.dataaccesslayer;

import com.example.banksys.model.Trade;
import org.springframework.data.repository.CrudRepository;

public interface TradeRepository extends CrudRepository<Trade, Long> {
}
