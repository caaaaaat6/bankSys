package com.example.banksys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

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


    @ToString.Exclude
    @OneToMany(mappedBy = "enterprise", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<EnterpriseUser> enterpriseUserList;

}
