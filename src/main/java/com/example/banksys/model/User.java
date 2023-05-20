package com.example.banksys.model;

import com.example.banksys.presentationlayer.utils.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
@Entity
@Table(name = "User", indexes = {@Index(columnList = "employeeId", unique = true)})
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("PersonalUser")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User implements UserDetails {

    public static final int PASSWORD_LENGTH = 512;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long userId;

    @Column(length = 20, nullable = false)
    protected String userPid;

    @Column(length = 32, nullable = false)
    protected String userName;

    @Column(length = 10, nullable = false)
    protected String userType;

    @Column
    private String password;

    @OneToOne(targetEntity = Card.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id")
    protected Card card;

    public User(String userPid, String userName, String password) {
        this.userPid = userPid;
        this.userName = userName;
        this.password = password;
    }

    public User(String userPid, String userName, String userType, String password) {
        this.userPid = userPid;
        this.userName = userName;
        this.userType = userType;
        this.password = password;
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        switch (getUserType()) {
            case Card.UserType.ORDINARY:
                list.add(new SimpleGrantedAuthority(Role.ORDINARY_USER));
                break;
            case Card.UserType.VIP:
                list.add(new SimpleGrantedAuthority(Role.VIP_USER));
                break;
            case Card.UserType.ENTERPRISE:
                list.add(new SimpleGrantedAuthority(Role.ENTERPRISE_USER));
            default:
        }
        switch (getCard().getCardType()) {
            case Card.CardType.CURRENT:
                list.add(new SimpleGrantedAuthority(Role.CURRENT_RIGHT));
                break;
            case Card.CardType.FIXED:
                list.add(new SimpleGrantedAuthority(Role.FIXED_RIGHT));
        }
        return list;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return getUserId() + "";
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
