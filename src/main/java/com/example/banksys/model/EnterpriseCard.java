package com.example.banksys.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("EnterpriseCard")
public class EnterpriseCard extends Card {

    @Basic
    private Long enterpriseId = null;

    public EnterpriseCard(long userId, String userPid, String userName, String userType, String password, Long enterpriseId, String cardType, double openMoney) {
        super(userId, userPid, userName, userType, password, cardType, openMoney);
        this.enterpriseId = enterpriseId;
    }
}
