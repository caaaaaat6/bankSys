package com.example.banksys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 个人银行卡模型
 */
@Entity
@Table(name = "Card")
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("PersonalCard")
@Data
@NoArgsConstructor(force = true)
public class PersonalCard extends Card{
    public PersonalCard(long userId, String userPid, String userName, String userType, String password, String cardType, double openMoney) {
        super(userId, userPid, userName, userType, password, cardType, openMoney);
    }
}
