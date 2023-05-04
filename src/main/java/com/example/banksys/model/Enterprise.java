package com.example.banksys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Enterprise")
@Data
@NoArgsConstructor
public class Enterprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long enterpriseId;

    @Column(length = 128, nullable = false)
    private String enterpriseName;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private long cardId;
}
