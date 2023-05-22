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

/**
 * 定期总管账号
 */
@Data
@NoArgsConstructor(force = true)
public class FixedHeadEmployeeAccount extends HeadEmployeeAccount {

    /**
     * 查询所有银行卡类型为定期的日志，包括非雇员产生的日志
     * @return 所有银行卡类型为定期的日志，包括非雇员产生的日志
     */
    @Override
    public List<AccountLog> findReport() {
        return findReportByCardType(Card.CardType.FIXED);
    }
}
