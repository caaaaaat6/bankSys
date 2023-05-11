package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.Trade;
import com.example.banksys.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@NoArgsConstructor(force = true)
public abstract class PersonalUserAccount extends BaseAccount {



    @Autowired
    private PersonalCardRepository personalCardRepository;

    private PersonalCard personalCard;

    public void setPersonalCard(PersonalCard personalCard) {
        this.personalCard = personalCard;
        setCard(personalCard);
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

    @Override
    public double transferMoneyTo(Card card, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException {
        String fromUserType = getCard().getUserType();
        String toUserType = card.getUserType();
        if (fromUserType.equals(Card.UserType.ORDINARY) && toUserType.equals(Card.UserType.ORDINARY)) {
            if (isSameUser(card)) {
//                _transferTo(card, money);
            }
        }
        return super.transferMoneyTo(card, money);
    }


    private boolean isSameUser(Card card) {
        String toUserPid = card.getUserPid();
        return getUser().getUserPid().equals(toUserPid);
    }
}
