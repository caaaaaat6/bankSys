package com.example.banksys.businesslogiclayer.useraccount;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.dataaccesslayer.EnterpriseCardRepository;
import com.example.banksys.dataaccesslayer.EnterpriseUserRepository;
import com.example.banksys.model.*;
import com.example.banksys.model.Exception.WithdrawException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@NoArgsConstructor(force = true)
@Service
public abstract class EnterpriseUserAccount extends BaseAccount {

    @Autowired
    private EnterpriseUserRepository enterpriseUserRepository;

    @Autowired
    protected EnterpriseCardRepository enterpriseCardRepository;

    protected EnterpriseUser enterpriseUser;

    protected EnterpriseCard enterpriseCard;

    protected Enterprise enterprise;

    public static final double BALANCE_THRESHOLD = 10000;

    Logger logger = LoggerFactory.getLogger(EnterpriseUserAccount.class);


    public void setEnterpriseCard(EnterpriseCard enterpriseCard) {
        setCard(enterpriseCard);
        this.enterpriseCard = enterpriseCard;
    }

    public void setEnterpriseUser(EnterpriseUser enterpriseUser) {
        setUser(enterpriseUser);
        this.enterpriseUser = enterpriseUser;
    }

    public long openEnterpriseAccount(long userId, String userPid, String userName, String password, Long enterpriseId, String cardType, double openMoney, Employee employee) {
        enterpriseCard = new EnterpriseCard(userId, userPid, userName, Card.UserType.ENTERPRISE, password, enterpriseId, cardType, openMoney);
        getEnterpriseUser().setEnterpriseCard(enterpriseCard);
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

    public boolean canWithdraw(double money) {
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

    @Override
    public double closeAccount()  {
        Enterprise enterprise = getEnterprise();

        double balance =  super.closeAccount();

        // 删除企业所有用户
        List<EnterpriseUser> enterpriseUserList = getEnterprise().getEnterpriseUserList();
        enterprise.setEnterpriseUserList(null); // 消除企业对企业用户的引用
        enterpriseUserList.forEach(e -> {e.setEnterprise(null);});
        getEnterpriseUserRepository().deleteAll(enterpriseUserList);


        return balance;
    }

    public Card getCard() {
        if (this.enterpriseCard != null) {
            return this.enterpriseCard;
        }
        if (this.getUser() == null) {
            return null;
        }
        return getUser().getCard();
    }

    public EnterpriseCard getEnterpriseCard() {
        if (this.enterpriseCard == null) {
            if (getCard() == null) {
                return null;
            }
            this.enterpriseCard = this.enterpriseCardRepository.findById(getCard().getCardId()).get();
        }
        return this.enterpriseCard;
    }

    public Enterprise getEnterprise() {
        if (this.enterprise == null) {
            this.enterprise = getEnterpriseUser().getEnterprise();
        }
        return this.enterprise;
    }

    public EnterpriseUser getEnterpriseUser() {
        if (this.enterpriseUser == null) {
            Long userId = getUser().getUserId();
            this.enterpriseUser = getEnterpriseUserRepository().findById(userId).get();
        }
        return this.enterpriseUser;
    }
}
