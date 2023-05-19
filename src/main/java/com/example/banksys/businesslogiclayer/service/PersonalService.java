package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.exception.EnterpriseWithdrawBalanceNotEnoughException;
import com.example.banksys.businesslogiclayer.exception.UntransferableException;
import com.example.banksys.businesslogiclayer.exception.VipOpenMoneyNotEnoughException;
import com.example.banksys.businesslogiclayer.useraccount.BaseAccount;
import com.example.banksys.businesslogiclayer.useraccount.BaseCurrentAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.BaseFixedAccountRight;
import com.example.banksys.businesslogiclayer.useraccount.PersonalUserAccount;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Exception.WithdrawException;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.Trade;
import com.example.banksys.model.User;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.form.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalService implements com.example.banksys.businesslogiclayer.service.Service {

    public static final double VIP_OPEN_THRESHOLD = 1000000;
    private PersonalCardRepository personalCardRepository;
    private UserRepository userRepository;

    public PersonalService(PersonalCardRepository personalCardRepository, UserRepository userRepository) {
        this.personalCardRepository = personalCardRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Long openAccount(PersonalUserAccount personalUserAccount,
                            PasswordEncoder passwordEncoder,
                            UserRepository userRepository,
                            OpenForm openForm
                            ) throws VipOpenMoneyNotEnoughException {
        if (openForm.getUserType().equals(Card.UserType.VIP)) {
            checkOpenMoney(openForm.getOpenMoney(), openForm.getUserPid());
        }
        String encoded = passwordEncoder.encode(openForm.getPassword());
        User user = userRepository.save(new User(openForm.getUserPid(), openForm.getUserName(), openForm.getUserType(),encoded));
        personalUserAccount.setUser(user);
        Long cardId = personalUserAccount.openAccount(
                user.getUserId(),
                openForm.getUserPid(),
                openForm.getUserName(),
                openForm.getUserType(),
                encoded,
                null,
                openForm.getCardType(),
                openForm.getOpenMoney(),
                null);
        return user.getUserId();
    }

    private void checkOpenMoney(double openMoney, String userPid) throws VipOpenMoneyNotEnoughException {
        Optional<List<PersonalCard>> personalCardsByUserPid = personalCardRepository.findPersonalCardsByUserPid(userPid);
        double sum = openMoney;
        if (personalCardsByUserPid.isPresent()) {
            for (PersonalCard personalCard : personalCardsByUserPid.get()) {
                sum += personalCard.getOpenMoney();
            }
        }
        if (sum < VIP_OPEN_THRESHOLD) {
            throw new VipOpenMoneyNotEnoughException("vip开户金额不能小于" + VIP_OPEN_THRESHOLD + "元！");
        }

        List<Long> userIds = new ArrayList<>();
        personalCardsByUserPid.get().forEach(personalCard -> {
            if (!personalCard.getUserType().equals(Card.UserType.VIP)){
                personalCard.setUserType(Card.UserType.VIP);
                userIds.add(personalCard.getUserId());
            }
        });
        Iterable<User> allById = userRepository.findAllById(userIds);
        allById.forEach(user -> user.setUserType(Card.UserType.VIP));
        userRepository.saveAll(allById); // 更新用户表
        personalCardRepository.saveAll(personalCardsByUserPid.get()); // 更新卡表
    }

    @Override
    @Transactional
    public double depositCurrent(BaseCurrentAccountRight accountRight, DepositCurrentForm form) {
        return accountRight.deposit(form.getMoney());
    }

    @Override
    @Transactional
    public double depositFixed(BaseFixedAccountRight accountRight, DepositFixedForm form) {
        return accountRight.deposit(form.getMoney(), form.getDepositDays());
    }

    @Override
    @Transactional
    public double withdraw(BaseAccount account, WithdrawForm form) throws EnterpriseWithdrawBalanceNotEnoughException, WithdrawException {
        return account.withdraw(form.getMoney());
    }

    @Override
    @Transactional
    public String queryBalance(BaseAccount account) {
        return account.queryBalance();
    }

    @Override
    @Transactional
    public List<AccountLog> queryQueryLogs(BaseAccount account) {
        return account.queryQueryLogs();
    }

    @Override
    @Transactional
    public List<Trade> queryTrades(BaseAccount account) {
        return account.queryTrades();
    }

    @Override
    @Transactional
    public double transfer(BaseAccount account, TransferForm transferForm) throws EnterpriseWithdrawBalanceNotEnoughException, UntransferableException, WithdrawException {
        User toUser = userRepository.findById(transferForm.getToUserId()).get();
        Card toCard = toUser.getCard();
        return account.transferMoneyTo(toCard,transferForm.getMoney());
    }

    @Override
    @Transactional
    public double close(BaseAccount account) {
        return account.closeAccount();
    }

}
