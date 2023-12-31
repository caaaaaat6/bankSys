package com.example.banksys.model;

import com.example.banksys.model.Exception.WithdrawException;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "Card")
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("Card")
@Inheritance
@Data
@NoArgsConstructor(force = true)
public class Card implements UserDetails {

    public static final String TEST = "test";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cardId;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 20, nullable = false)
    private String userPid;

    @Column(length = 32, nullable = false)
    private String userName;

    /**
     * @see UserType
     */
    @Column(length = 10)
    private String userType;

    @Column(length = 512, nullable = false)
    private String password;

    /**
     * @see CardType
     */
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return getCardId() + "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 用户类型：普通、VIp、企业
     */
    public static class UserType {
        public static final String ORDINARY = "ordinary";
        public static final String VIP = "vip";
        public static final String ENTERPRISE = "enterprise";
    }

    /**
     * 银行卡类型：定期、活期（建卡时需要）、取款、转入、转出（日志记录需要）
     */
    public static class CardType {
        public static final String FIXED = "fixed";
        public static final String CURRENT = "current";
        public static final String WITHDRAW = "withdraw";
        public static final String TRANSFER_IN = "transferIn";
        public static final String TRANSFER_OUT = "transferOut";
    }

    /**
     * 卡的取款
     * @param money
     * @return
     * @throws WithdrawException
     */
    public double withdraw(double money) throws WithdrawException {
        if (this.balance < money) {
            throw new WithdrawException("余额不足！");
        }
        this.balance -= money;
        return this.balance;
    }

    /**
     * 卡的存款
     * @param money
     * @return
     */
    public double deposit(double money) {
        this.balance += money;
        return this.balance;
    }
}
