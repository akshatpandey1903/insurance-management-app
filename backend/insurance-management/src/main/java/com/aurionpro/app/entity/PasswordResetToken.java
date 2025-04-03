package com.aurionpro.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name= "password_reset_token")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class PasswordResetToken 
{
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pass_id;
	
	@Column
	private String token;
	
	@OneToOne
	@JoinColumn(name="id",nullable=false)
	private User user;
	
	@Column
	private LocalDateTime expiryDate;
	
}
