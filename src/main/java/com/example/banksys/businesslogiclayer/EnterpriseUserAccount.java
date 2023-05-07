package com.example.banksys.businesslogiclayer;

import com.example.banksys.dataaccesslayer.EnterpriseCardRepository;
import com.example.banksys.model.EnterpriseCard;
import com.example.banksys.model.EnterpriseUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor(force = true)
@Service
public abstract class EnterpriseUserAccount extends BaseAccount {

    private EnterpriseUser enterpriseUser;

    protected EnterpriseCard enterpriseCard;

//    @Autowired
//    protected EnterpriseCardRepository enterpriseCardRepository;

//    public long openEnterpriseAccount(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney) {
//        enterpriseCard = new EnterpriseCard(userId, userPid, userName, userType, password, enterpriseId, cardType, openMoney);
////        long cardId = enterpriseCardRepository.save(enterpriseCard).getCardId();
//        long cardId = cardRepository.save(enterpriseCard).getCardId();
//        return cardId;
//    }
}
