package com.example.banksys.businesslogiclayer.util;

import com.example.banksys.dataaccesslayer.CardRepository;
import com.example.banksys.dataaccesslayer.TradeRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.Trade;

import java.util.Calendar;
import java.util.Date;

public class BLLUtil {

    /**
     * 活期存款
     * @param cardRepository
     * @param tradeRepository
     * @param card
     * @param money
     * @return 存款后余额
     */
    public static double currentDeposit(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money) {
        double newBalance = card.deposit(money);
        cardRepository.save(card);

        Trade trade = new Trade(card.getCardId(), null, Trade.TradeType.CURRENT, money, new Date());
        tradeRepository.save(trade);
        return newBalance;
    }

    /**
     * 定期存款
     * @param cardRepository
     * @param tradeRepository
     * @param card
     * @param money
     * @param depositDays 存款天数
     * @return 存款后余额
     */
    public static double fixedDeposit(CardRepository cardRepository, TradeRepository tradeRepository, Card card, double money, int depositDays) {
        double newBalance = card.deposit(money);
        cardRepository.save(card);

        Date expireDate = getExpireDate(depositDays);

        Trade trade = new Trade(card.getCardId(), null, Trade.TradeType.FIXED, money, expireDate, new Date());
        tradeRepository.save(trade);
        return newBalance;
    }

    /**
     * 得到定期存款的到期时间
     * @param depositDays
     * @return
     */
    public static Date getExpireDate(int depositDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, depositDays);
        Date expireDate = calendar.getTime();
        return expireDate;
    }

    /**
     * 查询总余额
     * @param card 银行卡
     * @return 总余额
     */
    public static double queryBalance(Card card) {
        return card.getBalance();
    }

    /**
     * 查询可取余额
     * @param tradeRepository
     * @param card 银行卡
     * @return 可取余额
     */
    public static double queryDesirableBalance(TradeRepository tradeRepository, Card card) {
        double fixedBalance = tradeRepository.findFixedBalance(card);
        return card.getBalance() - fixedBalance;
    }

    /**
     * 对可取余额和总余额进行格式化
     * @param desirableBalance
     * @param balance
     * @return 可取余额/总余额
     */
    public static String presentQueryBalanceResult(double desirableBalance, double balance) {
        return desirableBalance + "/" + balance;
    }

    /**
     * 从定期账户取钱
     * @param tradeRepository
     * @param cardRepository
     * @param card
     * @param employee
     * @param money
     * @return 取钱后的余额
     * @throws WithdrawException
     */
    public static double withdrawFixedAccount(TradeRepository tradeRepository, CardRepository cardRepository, Card card, Employee employee, double money) throws WithdrawException {
        double desirableBalance = BLLUtil.queryDesirableBalance(tradeRepository,card);
        if (desirableBalance < money) {
            throw new WithdrawException("定期账户可取余额不足" + money + "元！");
        }
        double oldBalance = card.getBalance();
        double newBalance = oldBalance - money;
        card.setBalance(newBalance);
        cardRepository.save(card);
        Trade trade = new Trade(card.getCardId(), employee, Trade.TradeType.WITHDRAW, money, new Date());
        tradeRepository.save(trade);
        return newBalance;
    }

    /**
     * 在转账前检查可取余额，而不是总余额，这一点和活期存款不同
     * @param tradeRepository
     * @param card
     * @param money
     * @throws WithdrawException
     */
    public static void checkDesirableBalanceBeforeTransfer(TradeRepository tradeRepository, Card card, double money) throws WithdrawException {
        if (BLLUtil.queryDesirableBalance(tradeRepository, card) < money) {
            throw new WithdrawException("可取余额不足" + money + "元！");
        }
    }
}
