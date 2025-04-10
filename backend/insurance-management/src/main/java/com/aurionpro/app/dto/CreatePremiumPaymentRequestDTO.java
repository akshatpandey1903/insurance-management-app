package com.aurionpro.app.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CreatePremiumPaymentRequestDTO {
	private int customerPolicyId;
	private BigDecimal amount;
}
