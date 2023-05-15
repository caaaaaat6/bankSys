package com.example.banksys.businesslogiclayer.service;

import com.example.banksys.businesslogiclayer.exception.VipOpenMoneyNotEnoughException;
import com.example.banksys.businesslogiclayer.useraccount.EnterpriseUserAccount;
import com.example.banksys.businesslogiclayer.useraccount.PersonalUserAccount;
import com.example.banksys.dataaccesslayer.EnterpriseCardRepository;
import com.example.banksys.dataaccesslayer.EnterpriseRepository;
import com.example.banksys.dataaccesslayer.EnterpriseUserRepository;
import com.example.banksys.dataaccesslayer.UserRepository;
import com.example.banksys.model.Card;
import com.example.banksys.model.Enterprise;
import com.example.banksys.model.EnterpriseUser;
import com.example.banksys.model.User;
import com.example.banksys.presentationlayer.utils.EnterpriseOpenForm;
import com.example.banksys.presentationlayer.utils.OpenForm;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnterpriseService {

    private EnterpriseCardRepository enterpriseCardRepository;
    private EnterpriseUserRepository enterpriseUserRepository;
    private EnterpriseRepository enterpriseRepository;

    public EnterpriseService(EnterpriseCardRepository enterpriseCardRepository, EnterpriseUserRepository enterpriseUserRepository, EnterpriseRepository enterpriseRepository) {
        this.enterpriseCardRepository = enterpriseCardRepository;
        this.enterpriseUserRepository = enterpriseUserRepository;
        this.enterpriseRepository = enterpriseRepository;
    }

    @Transactional
    public Long openAccount(EnterpriseUserAccount enterpriseUserAccount,
                            PasswordEncoder passwordEncoder,
                            EnterpriseOpenForm enterpriseOpenForm
    ) throws Exception {
        String encoded = passwordEncoder.encode(enterpriseOpenForm.getPassword());
        Enterprise enterprise = enterpriseRepository.findById(enterpriseOpenForm.getEnterpriseId()).get();
        EnterpriseUser enterpriseUser = enterpriseUserRepository.save(new EnterpriseUser(
                enterpriseOpenForm.getUserPid(),
                enterpriseOpenForm.getUserName(),
                encoded,
                enterprise
                ));
        enterpriseUserAccount.setEnterpriseUser(enterpriseUser);
        enterpriseUserAccount.setEnterprise(enterprise);
//        try {
            enterpriseUserAccount.openEnterpriseAccount(
                    enterpriseUser.getUserId(),
                    enterpriseOpenForm.getUserPid(),
                    enterpriseOpenForm.getUserName(),
                    encoded,
                    null,
                    enterpriseOpenForm.getCardType(),
                    enterpriseOpenForm.getOpenMoney(),
                    null);
//        } catch (Exception e) {
//            System.out.println("here are some errors");;
//        }
        return enterpriseUser.getUserId();
    }
}
