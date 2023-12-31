package com.example.banksys.model.log;

import com.example.banksys.model.Employee;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 日志记录模型
 */
@Entity
@Table(name = "AccountLog")
@Data
@NoArgsConstructor(force = true)
public class AccountLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private long cardId;

    @OneToOne
    @JoinColumn(name = "employeeId", referencedColumnName = "employeeId")
    private Employee employee;

    /**
     * @see OperationType
     */
    @Column(length = 64, nullable = false)
    private String operationType;

    @Column(length = 512,nullable = false)
    private String description;

    @Column(nullable = false)
    private Date date;

    public Long getEmployeeId() {
        if (employee == null) {
            return null;
        }
        return employee.getEmployeeId();
    }

    public AccountLog(Long userId, Employee employee, String operationType, String description) {
        this.userId = userId;
        this.employee = employee;
        this.operationType = operationType;
        this.description = description;
        this.date = new Date();
    }

    public AccountLog(Long userId, long cardId, Employee employee, String operationType, String description) {
        this.userId = userId;
        this.cardId = cardId;
        this.employee = employee;
        this.operationType = operationType;
        this.description = description;
        this.date = new Date();
    }

    /**
     * 操作类型：开户、活期存款、定期存款、取款、查询、转账、改密码、销户
     */
    public static class OperationType {
        public static final String OPEN = "开户";
        public static final String CURRENT_DEPOSIT = "活期存款";
        public static final String FIXED_DEPOSIT = "定期存款";
        public static final String WITHDRAW = "取款";
        public static final String QUERY = "查询";
        public static final String TRANSFER = "转账";
        public static final String CHANGE_PASSWORD = "改密码";
        public static final String CLOSE = "销户";
    }
}
