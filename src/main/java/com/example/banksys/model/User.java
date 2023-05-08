package com.example.banksys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLInsert;

// TODO
//   1.补充为数据库表
@Data
@NoArgsConstructor(force = true)
@Entity
@Table(name = "User")
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("PersonalUser")
public class User {

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

    @OneToOne(targetEntity = Card.class, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    protected Card card;

    public String getPassword() {
        return card.getPassword();
    }

    public void setPassword(String password) {
        card.setPassword(password);
    }
}
