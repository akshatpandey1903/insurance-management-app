package com.aurionpro.app.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "withdrawal_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRequest {
	
	@Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int withdrawalId;

    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    @Column(nullable = false)
    private String userRole;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WithdrawalStatus status = WithdrawalStatus.PENDING;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime requestedAt;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;
    
    @Column
    private LocalDateTime approvedAt;
    
    @Column
    private String remarks;
}
