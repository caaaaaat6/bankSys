package com.example.banksys.businesslogiclayer.useraccount;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.model.Card;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.PersonalCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class VIPUserAccount extends PersonalUserAccount {


    public static final double DEGRADE_THRESHOLD = 100000;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public double withdraw(double money) throws WithdrawException {

        boolean isDegrade = checkDegrade(money);
        if (isDegrade) {
            doDegrade();
        }
        double balance = super.withdraw(money);
        return balance;
    }

    /**
     * 完成降级，将所有个人卡降级为ordinary，并且将用户类型降级为ordinary
     */
    private void doDegrade() {
        String userPid = getPersonalCard().getUserPid();
        List<PersonalCard> personalCardsByUserPid = getPersonalCardRepository().findPersonalCardsByUserPid(userPid).get(); // 这样就保存不上了

        getPersonalCard().setUserType(Card.UserType.ORDINARY); // 猜测时因为User的卡没更新，还是vip，然后保存的时候级联保存，把User的card的userType又覆盖上去了
        personalCardsByUserPid.forEach(personalCard1 -> personalCard1.setUserType(Card.UserType.ORDINARY));
        getPersonalCardRepository().saveAll(personalCardsByUserPid);

        // 保存用户，更新用户的userType
        getUser().setUserType(Card.UserType.ORDINARY);
        getUserRepository().save(getUser());
    }

    /**
     * 判断Vip账户是否需要降级，通常由取款或者转账造成
     * @param money
     * @return Vip账户是否需要降级
     */
    private boolean checkDegrade(double money) {
        String userPid = getPersonalCard().getUserPid();
        List<PersonalCard> personalCardsByUserPid = getPersonalCardRepository().findPersonalCardsByUserPid(userPid).get();
        double sumBalance = 0;
        for (var personalCard : personalCardsByUserPid) {
            sumBalance += personalCard.getBalance();
        }
        if (sumBalance - money < DEGRADE_THRESHOLD) {
            return true;
        }
        return false;
    }

    /**
     * 完成降级，将所有个人卡降级为ordinary，并且将用户类型降级为ordinary
     */
    @Override
    public double transferMoneyTo(Card toCard, double money) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException, UntransferableException {
        if (checkDegrade(money)) {
            doDegrade();
        }
        return super.transferMoneyTo(toCard, money);
    }

}
