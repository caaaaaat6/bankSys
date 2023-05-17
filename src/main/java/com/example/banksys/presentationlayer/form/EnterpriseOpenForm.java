package com.example.banksys.presentationlayer.form;

import com.example.banksys.model.Card;
import com.example.banksys.presentationlayer.utils.PasswordMatches;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@PasswordMatches
@Slf4j
@Data
@NoArgsConstructor(force = true)
public class EnterpriseOpenForm extends OpenForm {

    {
        setUserType(Card.UserType.ENTERPRISE);
    }

    private Long enterpriseId;
    private String error;

//    public Enterprise getEnterprise()
}
