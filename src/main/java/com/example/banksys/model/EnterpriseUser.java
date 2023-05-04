package com.example.banksys.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("EnterpriseUser")
public class EnterpriseUser extends User {

    private Long enterpriseId;

    @Column(length = 32)
    private String root;

    public static class Root {
        public static final String SUPER = "super";
        public static final String ORDINARY = "ordinary";
    }
}
