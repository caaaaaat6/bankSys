package com.example.banksys.businesslogiclayer.employeeaccount;

import com.example.banksys.model.Card;
import com.example.banksys.model.log.AccountLog;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 活期总管账号
 */
@Data
@NoArgsConstructor(force = true)
public class CurrentHeadEmployeeAccount extends HeadEmployeeAccount {

    /**
     * 查询所有银行卡类型为活期的日志，包括非雇员产生的日志
     * @return 所有银行卡类型为活期的日志，包括非雇员产生的日志
     */
    @Override
    public List<AccountLog> findReport() {
        return findReportByCardType(Card.CardType.CURRENT);
    }
}
