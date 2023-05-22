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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "enterprise_id")
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
        switch (getRightType()) {
            case RightType.SUPER:
                list.add(new SimpleGrantedAuthority(Role.ENTERPRISE_SUPER_ROLE));
                break;
            case RightType.USER:
                list.add(new SimpleGrantedAuthority(Role.ENTERPRISE_ORDINARY_ROLE));
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

    public EnterpriseUser(String userPid, String userName, String userType, String password, String rightType, Enterprise enterprise, EnterpriseCard enterpriseCard) {
        super(userPid, userName, userType, password);
        this.rightType = rightType;
        this.enterprise = enterprise;
        this.enterpriseCard = enterpriseCard;
    }
}
