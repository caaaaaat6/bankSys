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

/**
 * 部门经理账号
 */
@Data
@NoArgsConstructor(force = true)
public class ManagerEmployeeAccount extends FrontDeskEmployeeAccount {

    @Autowired
    private CardRepository cardRepository;

    /**
     * 查询所管雇员的产生的流水报告
     * @return 所管雇员产生日志流水报告
     */
    @Override
    public List<AccountLog> findReport() {
        return getAccountLogRepository().findAllByEmployeeIn(getEmployee().getDepartment().getEmployeeList());
    }

    /**
     * 查询所管雇员信息
     * @return 所管雇员
     */
    @Override
    public List<Employee> findEmployeeManaged() {
        return getEmployee().getDepartment().getEmployeeList();
    }

    /**
     * 一个工具方法，负责按照定期活期来查询相应的日志，为银行活期定期总管服务
     * @param cardType 银行卡类型：定期/活期
     * @return 按照定期活期来查询相应的日志报告流水
     */
    public List<AccountLog> findReportByCardType(String cardType) {
        List<Card> cardsByCardType = getCardRepository().findCardsByCardType(cardType);
        List<Long> cardIds = new ArrayList<>();
        cardsByCardType.forEach(card -> cardIds.add(card.getCardId()));
        List<AccountLog> allByEmployeeInAndCardIdIn = getAccountLogRepository().findAllByCardIdInOrderByDateDesc(cardIds);
        return allByEmployeeInAndCardIdIn;
    }
}
