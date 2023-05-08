package com.example.banksys.model;

import com.example.banksys.model.Exception.WithdrawalException;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Card")
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("Card")
@Inheritance
@Data
@NoArgsConstructor(force = true)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cardId;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 20, nullable = false)
    private String userPid;

    @Column(length = 32, nullable = false)
    private String userName;

    @Column(length = 10, nullable = false)
    private String userType;

    @Column(length = 512, nullable = false)
    private String password;

    @Column(length = 16, nullable = false)
    private String cardType;

    @Column(nullable = false)
    private double openMoney;

    @Column(nullable = false)
    private double balance;

    public Card(long userId, String userPid, String userName, String userType, String password, String cardType, double openMoney) {
        this.userId = userId;
        this.userPid = userPid;
        this.userName = userName;
        this.userType = userType;
        this.password = password;
        this.cardType = cardType;
        this.openMoney = openMoney;
        this.balance = openMoney;
    }

    public static class UserType {
        public static final String ORDINARY = "ordinary";
        public static final String VIP = "vip";
        public static final String ENTERPRISE = "enterprise";
    }

    public static class CardType {
        public static final String FIXED = "fixed";
        public static final String CURRENT = "current";
        public static final String WITHDRAWAL = "withdrawal";
    }

    public double withdrawal(double money) throws WithdrawalException {
        if (this.balance < money) {
            throw new WithdrawalException("余额不足！");
        }
        this.balance -= money;
        return this.balance;
    }

    public double save(double money) {
        this.balance += money;
        return this.balance;
    }
}
