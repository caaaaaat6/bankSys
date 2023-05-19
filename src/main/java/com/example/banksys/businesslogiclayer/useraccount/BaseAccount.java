package com.example.banksys.businesslogiclayer.useraccount;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.util.BLLUtil;
import com.example.banksys.dataaccesslayer.AccountLogRepository;
import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.Trade;
import com.example.banksys.model.User;
import com.example.banksys.model.log.AccountLog;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public abstract class BaseAccount implements BaseAccountRight {

    private User user;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "employeeId")
    private Employee employee;

    private Card card;

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
    public long openAccount(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney, Employee employee) {
        card = new Card(userId, userPid, userName, userType, password, cardType, openMoney);
        long cardId = cardRepository.save(card).getCardId();
        return cardId;
    }


    // TODO
    //  1.待测试
    //  3.是否需要原子性
    @Transactional
    @Override
    public double withdraw(double money) throws WithdrawException, EnterpriseWithdrawBalanceNotEnoughException {
        double balance = getCard().withdraw(money);
        cardRepository.save(getCard());
        Trade trade = new Trade(getCard().getCardId(), employee, Trade.TradeType.WITHDRAW, money, new Date());
        tradeRepository.save(trade);
        return balance;
    }

    /**
     * @return 返回 可取余额/总余额
     */
    @Override
    public String queryBalance() {
        double balance = BLLUtil.queryBalance(getCard());
        double desirableBalance = BLLUtil.queryDesirableBalance(getTradeRepository(), getCard());
        return BLLUtil.presentQueryBalanceResult(desirableBalance, balance);
    }

    @Override
    public List<Trade> queryTrades() {
        return getTradeRepository().findAllByCardIdOrderByTradeDateDesc(getCard().getCardId());
    }

    @Override
    public List<AccountLog> queryQueryLogs() {
        return getAccountLogRepository().findAllByCardIdAndOperationTypeOrderByDateDesc(getCard().getCardId(), AccountLog.OperationType.QUERY);
    }

    @Override
    public double transferMoneyTo(Card toCard, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        double newBalance = getCard().withdraw(money);
        getCardRepository().save(getCard());

        toCard.deposit(money);
        getCardRepository().save(toCard);

        Trade transferOutTrade = new Trade(card.getCardId(), employee, Trade.TradeType.TRANSFER_OUT, money, new Date(), toCard.getCardId());
        Trade transferInTrade = new Trade(toCard.getCardId(), employee, Trade.TradeType.TRANSFER_IN, money, new Date(), getCard().getCardId());
        tradeRepository.saveAll(Arrays.asList(transferOutTrade, transferInTrade));

        return newBalance;
    }

    @Override
    public void changePassWord(String newPassword) {
        getUser().setPassword(newPassword);
        userRepository.save(getUser());
    }

    @Override
    public double closeAccount() {
        double balance = getCard().getBalance();
        try {
            getCard().withdraw(balance);
        } catch (WithdrawException e) {
            e.printStackTrace();
        }

        // 删除卡的信息
        getCardRepository().delete(getCard());

        // 删除卡对应的用户信息
        getUserRepository().delete(getUser());

        return balance;
    }

    public void setUser(User user) {
        this.user = user;
        this.card = user.getCard();
    }

    public Card getCard() {
        return getUser().getCard();
    }
}
