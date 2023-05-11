package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.Trade;
import com.example.banksys.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
@Service
public abstract class BaseAccount implements BaseAccountRight{

    private User user;

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
    @Transactional
    @Override
    public double withdraw(double money) throws WithdrawException, EnterpriseWithdrawBalanceNotEnoughException {
        double balance = card.withdraw(money);
        cardRepository.save(card);
        Trade trade = new Trade(card.getCardId(), employeeId, Trade.TradeType.WITHDRAW,money,new Date());
        tradeRepository.save(trade);
        return balance;
    }

    /**
     *
     * @return 返回 可取余额/总余额
     */
    @Override
    public String queryBalance() {
        double balance = BLLUtil.queryBalance(getCard());
        double desirableBalance = BLLUtil.queryDesirableBalance(getTradeRepository(), getCard());
        return BLLUtil.presentQueryBalanceResult(desirableBalance,balance);
    }

    @Override
    public List<Trade> queryDaybook(Date start, Date end) {
        return null;
    }

    @Override
    public double transferMoneyTo(Card toCard, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException {
        double newBalance = card.withdraw(money);
        getCardRepository().save(card);

        toCard.deposit(money);
        getCardRepository().save(toCard);

        Trade transferOutTrade = new Trade(card.getCardId(), getEmployeeId(), Trade.TradeType.TRANSFER_OUT, money, new Date(), toCard.getCardId());
        Trade transferInTrade = new Trade(toCard.getCardId(), getEmployeeId(), Trade.TradeType.TRANSFER_IN, money, new Date(), getCard().getCardId());
        tradeRepository.saveAll(Arrays.asList(transferOutTrade, transferInTrade));

        return newBalance;
    }

    @Override
    public void changePassWord() {

    }

    @Override
    public void closeAccount() {

    }

}
