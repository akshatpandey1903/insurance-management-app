package com.aurionpro.app.dto;

import java.math.BigDecimal;

public class AgentCommissionReportDto {

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private Long totalPoliciesSold;
    private BigDecimal totalCommissionRate;
    private BigDecimal totalEarnings;

    // Constructor must match the SELECT clause exactly
    public AgentCommissionReportDto(int userId, String firstName, String lastName, String email,
                                    Long totalPoliciesSold, BigDecimal totalCommissionRate, BigDecimal totalEarnings) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.totalPoliciesSold = totalPoliciesSold;
        this.totalCommissionRate = totalCommissionRate;
        this.totalEarnings = totalEarnings;
    }

    // Getters (optional if you’re using @RestController and Jackson)
    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Long getTotalPoliciesSold() {
        return totalPoliciesSold;
    }

    public BigDecimal getTotalCommissionRate() {
        return totalCommissionRate;
    }

    public BigDecimal getTotalEarnings() {
        return totalEarnings;
    }
}
