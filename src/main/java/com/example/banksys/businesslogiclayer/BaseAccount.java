package com.example.banksys.businesslogiclayer;

import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.Trade;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
@Service
public abstract class BaseAccount implements BaseAccountRight{

    protected Long employeeId = null;

    protected Card card;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CardRepository cardRepository;

    @Autowired
    protected TradeRepository tradeRepository;

    @Autowired
    protected AccountLogRepository accountLogRepository;

    // TODO
    //  1.需要检查身份证号码是否符合规则
    //  2.待测试1.
    //  4.收集该用户所有卡的开卡金额，判断是否升级为VIP用户(只在个人用户中判断,在service中实现）
    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney, Long employeeId) {
        card = new Card(userId, userPid, userName, userType, password, cardType, openMoney);
        long cardId = cardRepository.save(card).getCardId();
        return cardId;
    }


    // TODO
    //  1.待测试
    //  2.在vip用户执行前还需要判断是否需要对用户降级
    //  3.是否需要原子性
    //  4.产生日志
    @Transactional
    @Override
    public double withdraw(double money) throws WithdrawException {
        double balance = card.withdraw(money);
        cardRepository.save(card);
        Trade trade = new Trade(card.getCardId(), employeeId, Trade.TradeType.WITHDRAW,money,new Date());
        tradeRepository.save(trade);
        return balance;
    }

    @Override
    public double queryBalance() {
        return 0;
    }

    @Override
    public List<Trade> queryDaybook(Date start, Date end) {
        return null;
    }

    @Override
    public void transferMoneySelf() {

    }

    @Override
    public void changePassWord() {

    }

    @Override
    public void closeAccount() {

    }

}
