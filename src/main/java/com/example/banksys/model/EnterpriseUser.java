package com.example.banksys.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("EnterpriseUser")
public class EnterpriseUser extends User {

//    private Long enterpriseId;

    @Column(length = 32)
    private String rightType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    public static class RightType {
        public static final String SUPER = "super";
        public static final String USER = "user";
    }
}
