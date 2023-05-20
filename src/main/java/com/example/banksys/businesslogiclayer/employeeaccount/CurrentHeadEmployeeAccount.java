package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.model.Card;
import com.example.banksys.model.log.AccountLog;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class CurrentHeadEmployeeAccount extends HeadEmployeeAccount {

    @Override
    public List<AccountLog> findReport() {
        return findReportByCardType(Card.CardType.CURRENT);
    }
}
