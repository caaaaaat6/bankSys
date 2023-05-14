package com.example.banksys.model;

import com.example.banksys.presentationlayer.utils.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLInsert;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

    @OneToOne(targetEntity = Card.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    protected Card card;


    public User(String userPid, String userName, String userType) {
        this.userPid = userPid;
        this.userName = userName;
        this.userType = userType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        switch (getUserType()) {
            case Card.UserType.ORDINARY:
                list.add(new SimpleGrantedAuthority(Role.ORDINARY_USER));
                break;
            case Card.UserType.VIP:
                list.add(new SimpleGrantedAuthority(Role.VIP_USER));
            default:
        }
        return list;
    }

//    public String getPassword() {
//        return card.getPassword();
//    }



//    public void setPassword(String password) {
//        if (card != null) {
//            card.setPassword(password);
//        }
//    }

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
}
