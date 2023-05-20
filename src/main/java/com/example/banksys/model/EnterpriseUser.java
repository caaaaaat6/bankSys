package com.example.banksys.model;

import com.example.banksys.presentationlayer.utils.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("EnterpriseUser")
public class EnterpriseUser extends User {

//    private Long enterpriseId;

    @Column(length = 32)
    private String rightType;

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "enterprise_card_id")
    private EnterpriseCard enterpriseCard;

    @Column(length = 512)
    private String enterprise_card_password;

    public static class RightType {
        public static final String SUPER = "super";
        public static final String USER = "user";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        switch (getUserType()) {
            case Card.UserType.ENTERPRISE:
                list.add(new SimpleGrantedAuthority(Role.ENTERPRISE_USER_ROLE));
                break;
            default:
        }
        return list;
    }

    public EnterpriseUser(String userPid, String userName, String password, Enterprise enterprise) {
        super(userPid, userName, password);
        this.userType = Card.UserType.ENTERPRISE;
        this.enterprise = enterprise;
    }

    public Card getCard() {
        return this.enterpriseCard;
    }

    public void setEnterpriseCard(EnterpriseCard enterpriseCard) {
        this.enterpriseCard = enterpriseCard;
        setCard(enterpriseCard);
    }
}
