package com.example.banksys.model;

import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.model.Exception.WithdrawalException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Data
public abstract class BaseAccount implements BaseAccountRight{

    private Long employeeId = null;

    protected Card card;

    @Autowired
    protected CardRepository cardRepository;

    @Autowired
    protected TradeRepository tradeRepository;

    @Autowired
    protected AccountLogRepository accountLogRepository;

    // TODO
    //  1.需要检查身份证号码是否符合规则
    //  2.待测试1.
    //  3.产生日志
    //  4.收集该用户所有卡的开卡金额，判断是否升级为VIP用户(只在个人用户中判断）
//    @Override
//    public long openAccount(long userId, String userPid, String userName, String userType, String password, String cardType, double openMoney) {
//        card = new Card(userId, userPid, userName, userType, password, cardType, openMoney);
//        long cardId = cardRepository.save(card).getCardId();
//        return cardId;
//    }


    // TODO
    //  1.待测试
    //  2.在vip用户执行前还需要判断是否需要对用户降级
    //  3.是否需要原子性
    //  4.产生日志
    @Override
    public double withdrawal(double money) throws WithdrawalException {
        double balance = card.withdrawal(money);
        cardRepository.save(card);
        Trade trade = new Trade(card.getCardId(), employeeId, Trade.TradeType.WITHDRAWAL,money,new Date());
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
    public void closeAccount() {

    }
}
