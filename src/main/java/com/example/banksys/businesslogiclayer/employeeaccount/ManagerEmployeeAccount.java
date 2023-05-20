package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.log.AccountLog;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class ManagerEmployeeAccount extends FrontDeskEmployeeAccount {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<AccountLog> findReport() {
        return getAccountLogRepository().findAllByEmployeeIn(getEmployee().getDepartment().getEmployeeList());
    }

    @Override
    public List<Employee> findEmployeeManaged() {
        return getEmployee().getDepartment().getEmployeeList();
    }

    public List<AccountLog> findReportByCardType(String cardType) {
        List<Card> cardsByCardType = getCardRepository().findCardsByCardType(Card.CardType.FIXED);
        List<Long> cardIds = new ArrayList<>();
        cardsByCardType.forEach(card -> cardIds.add(card.getCardId()));
        List<AccountLog> allByEmployeeInAndCardIdIn = getAccountLogRepository().findAllByEmployeeInAndCardIdIn(getEmployee().getDepartment().getEmployeeList(), cardIds);
        return allByEmployeeInAndCardIdIn;
    }
}
