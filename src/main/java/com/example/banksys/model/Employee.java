package com.example.banksys.model;

import com.example.banksys.businesslogiclayer.employeeaccount.EmployeeRight;
import com.example.banksys.dataaccesslayer.EmployeeRepository;
import com.example.banksys.model.log.AccountLog;
import com.example.banksys.presentationlayer.utils.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
//@Table(name = "Employee")
//@DiscriminatorColumn(name = "employeeType", discriminatorType = DiscriminatorType.STRING, length = 30)
//@DiscriminatorValue(Employee.EmployeeType.FRONT_DESK)
@DiscriminatorValue("Employee")
@Data
@NoArgsConstructor(force = true)
public class Employee extends User implements EmployeeRight, UserDetails {

//    @Column(columnDefinition = "bigint UNIQUE key auto_increment")
    private Long employeeId;

    @Column(length = 16)
    private String employeeType;

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Department department;

    private boolean enabled;

    public Employee(String employeeType, String employeeName, String password) {
        this.employeeType = employeeType;
        setUserName(employeeName);
        setPassword(password);
    }

    public Employee(String employeeType, String employeeName,String pid, Department department, String password) {
        this.employeeType = employeeType;
        setUserName(employeeName);
        this.department = department;
        setUserPid(pid);
        setPassword(password);
        if (employeeType.equals(Employee.EmployeeType.ADMIN_EN)) {
            this.enabled = true;
            this.department = null;
        }
    }

    public static class EmployeeType {
        public static final String FRONT_DESK = "前台操作员";
        public static final String MANAGER = "银行经理";
        public static final String CURRENT_HEAD = "活期银行业务总管";
        public static final String FIXED_HEAD = "定期银行业务总管";
        public static final String ADMIN = "系统管理员";

        public static final String FRONT_DESK_EN = "frontDesk";
        public static final String MANAGER_EN = "manager";
        public static final String CURRENT_HEAD_EN = "currentHead";
        public static final String FIXED_HEAD_EN = "fixedHead";
        public static final String ADMIN_EN = "admin";
    }

    @Override
    public List<AccountLog> findReport() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(Role.EMPLOYEE_ROLE));
        list.add(new SimpleGrantedAuthority(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR));
        switch (getEmployeeType()) {
            case EmployeeType.FRONT_DESK_EN:
                list.add(new SimpleGrantedAuthority(Role.FRONT_DESK_EMPLOYEE_ROLE));
                break;
            case EmployeeType.MANAGER_EN:
                list.add(new SimpleGrantedAuthority(Role.MANAGER_EMPLOYEE_ROLE));
                break;
            case EmployeeType.CURRENT_HEAD_EN:
                list.add(new SimpleGrantedAuthority(Role.CURRENT_HEAD_EMPLOYEE_ROLE));
                break;
            case EmployeeType.FIXED_HEAD_EN:
                list.add(new SimpleGrantedAuthority(Role.FIXED_HEAD_EMPLOYEE_ROLE));
                break;
            case EmployeeType.ADMIN_EN:
                list.add(new SimpleGrantedAuthority(Role.SYSTEM_ADMIN_ROLE));
                break;
            default:
                break;
        }
        return list;
    }

    /**
     * 系统管理员不需要审核
     */
    @Override
    public boolean isEnabled() {
        if (getEmployeeType().equals(EmployeeType.ADMIN_EN)) {
            this.enabled = true;
        }
        return enabled;
    }

    public boolean getEnabled() {
        if (getEmployeeType().equals(EmployeeType.ADMIN_EN)) {
            this.enabled = true;
        }
        return enabled;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
        this.enabled = true;
    }

    public Long getEmployeeId() {
        if (employeeId == null) {
            this.employeeId = getUserId();
        }
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        this.employeeId = userId;
    }
}
