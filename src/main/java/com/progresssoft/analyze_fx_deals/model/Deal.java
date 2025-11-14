package com.progresssoft.analyze_fx_deals.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deal{

    @Id
    private String dealUniqueId;

    @Column(nullable = false)
    private String fromCurrencyIsoCode;

    @Column(nullable = false)
    private String toCurrencyIsoCode;

    @Column(nullable = false)
    private LocalDateTime dealTimestamp;

    @Column(nullable = false)
    private BigDecimal dealAmount;

}