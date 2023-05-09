package com.example.banksys.businesslogiclayer;

import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.Trade;
import com.example.banksys.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@NoArgsConstructor(force = true)
public abstract class PersonalUserAccount extends BaseAccount {

    protected User user;

    @Autowired
    private PersonalCardRepository personalCardRepository;

    private PersonalCard personalCard;

    public void setPersonalCard(PersonalCard personalCard) {
        this.personalCard = personalCard;
        this.card = personalCard;
    }

    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney, Long employeeId) {
        personalCard = new PersonalCard(userId, userPid, userName, userType, password, cardType, openMoney);
        long cardId = personalCardRepository.save(personalCard).getCardId();
        return cardId;
    }

    @Override
    public double withdraw(double money) throws WithdrawException {
        double balance = personalCard.withdraw(money);
        personalCardRepository.save(personalCard);
        Trade trade = new Trade(personalCard.getCardId(), employeeId, Trade.TradeType.WITHDRAW, money, new Date());
        tradeRepository.save(trade);
        return balance;
    }
}
