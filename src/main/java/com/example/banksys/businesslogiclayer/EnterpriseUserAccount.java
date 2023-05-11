package com.example.banksys.businesslogiclayer;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.dataaccesslayer.EnterpriseCardRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseCard;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.log.AccountLog;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor(force = true)
@Service
public abstract class EnterpriseUserAccount extends BaseAccount {

    protected EnterpriseUser enterpriseUser;

    protected EnterpriseCard enterpriseCard;

    protected Enterprise enterprise;

    public static final double BALANCE_THRESHOLD = 10000;

    Logger logger = LoggerFactory.getLogger(EnterpriseUserAccount.class);

    public void setEnterpriseCard(EnterpriseCard enterpriseCard) {
        this.card = enterpriseCard;
        this.enterpriseCard = enterpriseCard;
    }

    public void setEnterpriseUser(EnterpriseUser enterpriseUser) {
        setUser(enterpriseUser);
        this.enterpriseUser = enterpriseUser;
    }

    @Autowired
    protected EnterpriseCardRepository enterpriseCardRepository;

    public long openEnterpriseAccount(long userId, String userPid, String userName, String password, Long enterpriseId, String cardType, double openMoney, Long employeeId) {
        enterpriseCard = new EnterpriseCard(userId, userPid, userName, Card.UserType.ENTERPRISE, password, enterpriseId, cardType, openMoney);
        long cardId = cardRepository.save(enterpriseCard).getCardId();
        return cardId;
    }

    @Override
    public double withdraw(double money) throws WithdrawException, EnterpriseWithdrawBalanceNotEnoughException {
        if (!canWithdraw(money)) {
            throw new EnterpriseWithdrawBalanceNotEnoughException("企业用户剩余存款余额不足" + BALANCE_THRESHOLD + "元，无法取款！");
        }
        double newBalance =  super.withdraw(money);
        return newBalance;
    }

    private boolean canWithdraw(double money) {
        double balance = getEnterpriseCard().getBalance();
        if (balance - money >= BALANCE_THRESHOLD) {
            return true;
        }
        return false;
    }

    @Override
    public double transferMoneyTo(Card toCard, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        if (!transferableTo(toCard)) {
            throw new UntransferableException("企业用户不能向个人用户转账！");
        }
        return super.transferMoneyTo(toCard, money);
    }

    private boolean transferableTo(Card toCard) {
        String toCardUserType = toCard.getUserType();
        if (toCardUserType.equals(Card.UserType.ORDINARY) || toCardUserType.equals(Card.UserType.VIP)) {
            return false;
        }
        return true;
    }
}
