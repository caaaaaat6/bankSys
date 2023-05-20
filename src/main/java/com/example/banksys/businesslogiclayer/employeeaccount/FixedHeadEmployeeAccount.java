package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.log.AccountLog;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class FixedHeadEmployeeAccount extends HeadEmployeeAccount {

    @Override
    public List<AccountLog> findReport() {
        return findReportByCardType(Card.CardType.FIXED);
    }
}
