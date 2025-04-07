package com.aurionpro.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "customer_documents")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomerDocument {
	
	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int documentId;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private Customer customer;
	
	@Column(nullable = false)
	private DocumentType documentType;
	
	@Column(nullable = false)
	private String documentUrl;
	
	@Column(nullable = false)
	private boolean isVerified = false;
	
	@CreationTimestamp
	@Column
	private LocalDateTime uploadedAt;
	
	@ManyToOne
    @JoinColumn(name = "verified_by", referencedColumnName = "user_id", nullable = true)
    private Employee verifiedBy;

    @Column
    private LocalDateTime verifiedAt;
    
    @Column(nullable = false)
    private boolean isDeleted = false;

}
