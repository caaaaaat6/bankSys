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

/**
 * 用户账户基类
 */
@Data
@NoArgsConstructor(force = true)
@Service
public abstract class BaseAccount implements BaseAccountRight {

    /**
     * 账户持有账户对应的用户
     */
    private User user;

    /**
     * 账户持有为账户操作的雇员，若无，则为null
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "employeeId")
    private Employee employee;

    /**
     * 账户持有账户对应的银行卡
     */
    private Card card;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CardRepository cardRepository;

    @Autowired
    protected TradeRepository tradeRepository;

    @Autowired
    protected AccountLogRepository accountLogRepository;

    /**
     * 开户
     * @param userId
     * @param userPid
     * @param userName
     * @param userType
     * @param password
     * @param enterpriseId
     * @param cardType
     * @param openMoney
     * @param employee
     * @return 所新开账户的ID
     */
    @Override
    @Transactional
    public long openAccount(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney, Employee employee) {
        card = new Card(userId, userPid, userName, userType, password, cardType, openMoney);
        long cardId = cardRepository.save(card).getCardId();
        return cardId;
    }


    /**
     * 取款
     * @param money 取款金额
     * @return 取款后余额
     * @throws WithdrawException 取款后余额为负异常
     * @throws EnterpriseWithdrawBalanceNotEnoughException 企业用户取款后余额不足门限异常
     */
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
     * 查询余额
     * @return 返回 可取余额/总余额
     */
    @Override
    public String queryBalance() {
        double balance = BLLUtil.queryBalance(getCard());
        double desirableBalance = BLLUtil.queryDesirableBalance(getTradeRepository(), getCard());
        return BLLUtil.presentQueryBalanceResult(desirableBalance, balance);
    }

    /**
     * 查询交易明细
     * @return 交易明细
     */
    @Override
    public List<Trade> queryTrades() {
        return getTradeRepository().findAllByCardIdOrderByTradeDateDesc(getCard().getCardId());
    }

    /**
     * 查询以前查询产生的日志
     * @return 查询日志
     */
    @Override
    public List<AccountLog> queryQueryLogs() {
        return getAccountLogRepository().findAllByCardIdAndOperationTypeOrderByDateDesc(getCard().getCardId(), AccountLog.OperationType.QUERY);
    }

    /**
     * 转账
     * @param toCard 转入的银行卡
     * @param money 转账金额
     * @return
     * @throws EnterpriseWithdrawBalanceNotEnoughException 企业用户取款后余额不足门限异常
     * @throws WithdrawException 取款后余额为负异常
     * @throws UntransferableException 无法转账异常
     */
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

    /**
     * 改密码
     * @param newEncodedPassword 新的已加密的密码
     */
    @Override
    public void changePassword(String newEncodedPassword) {
        getUser().setPassword(newEncodedPassword);
        userRepository.save(getUser());
    }

    /**
     * 销户
     * @return 销户时取出的余额
     */
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
