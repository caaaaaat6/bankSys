package com.example.banksys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO
//   1.补充为数据库表
@Data
@NoArgsConstructor(force = true)
@Entity
@Table(name = "User")
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING, length = 30)
@DiscriminatorValue("PersonalUser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long userId;

    @Column(length = 20, nullable = false)
    protected String userPid;

    @Column(length = 32, nullable = false)
    protected String userName;

    @Column(length = 10, nullable = false)
    protected String userType;

}
