package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.exception.VipOpenMoneyNotEnoughException;
import com.example.banksys.businesslogiclayer.useraccount.PersonalUserAccount;
import com.example.banksys.dataaccesslayer.PersonalCardRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.PersonalCard;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.utils.OpenForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalService {

//    private PersonalUserAccount personalUserAccount;
//
//    @Autowired
//    public void personalUserAccount(PersonalUserAccount personalUserAccount) {
//        this.personalUserAccount = personalUserAccount;
//    }

    public static final double VIP_OPEN_THRESHOLD = 1000000;

    private PersonalCardRepository personalCardRepository;
    private UserRepository userRepository;

    public PersonalService(PersonalCardRepository personalCardRepository, UserRepository userRepository) {
        this.personalCardRepository = personalCardRepository;
        this.userRepository = userRepository;
    }

    public Long openAccount(PersonalUserAccount personalUserAccount,
                            PasswordEncoder passwordEncoder,
                            UserRepository userRepository,
                            OpenForm openForm
                            ) throws VipOpenMoneyNotEnoughException {
        if (openForm.getUserType().equals(Card.UserType.VIP)) {
            checkOpenMoney(openForm.getOpenMoney(), openForm.getUserPid());
        }
        User user = userRepository.save(new User(openForm.getUserPid(), openForm.getUserName(), openForm.getUserType()));
        personalUserAccount.setUser(user);
        Long cardId = personalUserAccount.openAccount(
                user.getUserId(),
                openForm.getUserPid(),
                openForm.getUserName(),
                openForm.getUserType(),
                passwordEncoder.encode(openForm.getPassword()),
                null,
                openForm.getCardType(),
                openForm.getOpenMoney(),
                null);
        return cardId;
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

}
