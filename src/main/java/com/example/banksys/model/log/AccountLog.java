package com.example.banksys.model.log;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

// TODO
//  1.待完成@Table等注释
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

    private Long employeeId;

    @Column(length = 64, nullable = false)
    private String operationType;

    @Column(length = 512,nullable = false)
    private String description;

    @Column(nullable = false)
    private Date date;

    public AccountLog(Long userId, Long employeeId, String operationType, String description) {
        this.userId = userId;
        this.employeeId = employeeId;
        this.operationType = operationType;
        this.description = description;
        this.date = new Date();
    }

    public AccountLog(Long userId, long cardId, Long employeeId, String operationType, String description) {
        this.userId = userId;
        this.cardId = cardId;
        this.employeeId = employeeId;
        this.operationType = operationType;
        this.description = description;
        this.date = new Date();
    }

    public static class OperationType {
        public static final String OPEN = "开户";
        public static final String CURRENT_DEPOSIT = "活期存款";
        public static final String FIXED_DEPOSIT = "定期存款";
        public static final String WITHDRAWAL = "取款";
        public static final String QUERY = "查询";
        public static final String CHANGE_PASSWORD = "改密码";
        public static final String CLOSE = "销户";
    }
}
