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
    private Long employeeId;

    @Column(length = 64, nullable = false)
    private String operationType;

    @Column(length = 512,nullable = false)
    private String description;

    @Column(nullable = false)
    private Date date;
}
