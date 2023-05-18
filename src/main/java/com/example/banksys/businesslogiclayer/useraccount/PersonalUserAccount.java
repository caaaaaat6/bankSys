package com.example.banksys.businesslogiclayer.useraccount;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Employee;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.Trade;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
@NoArgsConstructor(force = true)
public abstract class PersonalUserAccount extends BaseAccount {



    @Autowired
    private PersonalCardRepository personalCardRepository;

//    @OneToOne
    private PersonalCard personalCard;

    public void setPersonalCard(PersonalCard personalCard) {
        this.personalCard = personalCard;
        setCard(personalCard);
    }

    @Override
    public long openAccount(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney, Employee employee) {
        personalCard = new PersonalCard(userId, userPid, userName, userType, password, cardType, openMoney);
        getUser().setCard(personalCard);
        long cardId = personalCardRepository.save(personalCard).getCardId();
        return cardId;
    }

    @Override
    public double withdraw(double money) throws WithdrawException {
        double balance = getPersonalCard().withdraw(money);
        personalCardRepository.save(personalCard);
        Trade trade = new Trade(getPersonalCard().getCardId(), getEmployee(), Trade.TradeType.WITHDRAW, money, new Date());
        tradeRepository.save(trade);
        return balance;
    }

    @Override
    public double transferMoneyTo(Card toCard, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        if (!transferableTo(toCard)) {
            throw new UntransferableException("个人账户不能向企业用户转账！");
        }
        return super.transferMoneyTo(toCard, money);
    }

    private boolean transferableTo(Card toCard) {
        if (toCard.getUserType().equals(Card.UserType.ENTERPRISE)) {
            return false;
        }
        return true;
    }

    @Override
    public Card getCard() {
        if (this.personalCard != null) {
            return personalCard;
        }
        if (getUser() == null) {
            return null;
        }
        return getUser().getCard();
    }

    public PersonalCard getPersonalCard() {
        if (personalCard == null) {
            if (getCard() == null) {
                return null;
            }
            this.personalCard = getPersonalCardRepository().findById(getCard().getCardId()).get();
        }
        return personalCard;
    }

}
