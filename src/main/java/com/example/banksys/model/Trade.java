package com.example.banksys.model;

import jakarta.annotation.Generated;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Trade")
@Data
@NoArgsConstructor(force = true)
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tradeId;

    @Column(nullable = false)
    private Long cardId;

    @Column(nullable = true)
    private Long employeeId;

    @Column(length = 16, nullable = false)
    private String tradeType;

    @Column(nullable = false)
    private double money;

    @Column(nullable = false)
    private Date expireDate;

    @Column(nullable = false)
    private Date tradeDate;

    private Long transferCardId;

    public static class TradeType extends Card.CardType {

    }

    public Trade(Long cardId, Long employeeId, String tradeType, double money, Date tradeDate) {
        this.cardId = cardId;
        this.employeeId = employeeId;
        this.tradeType = tradeType;
        this.money = money;
        this.expireDate = tradeDate;
        this.tradeDate = tradeDate;
    }

    public Trade(Long cardId, Long employeeId, String tradeType, double money, Date expireDate, Date tradeDate) {
        this.cardId = cardId;
        this.employeeId = employeeId;
        this.tradeType = tradeType;
        this.money = money;
        this.expireDate = expireDate;
        this.tradeDate = tradeDate;
    }

    public Trade(Long cardId, Long employeeId, String tradeType, double money, Date tradeDate, Long transferCardId) {
        this.cardId = cardId;
        this.employeeId = employeeId;
        this.tradeType = tradeType;
        this.money = money;
        this.expireDate = tradeDate;
        this.tradeDate = tradeDate;
        this.transferCardId = transferCardId;
    }
}
