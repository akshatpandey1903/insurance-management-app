package com.aurionpro.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentOrderResponseDTO {
	private String orderId;
	private String currency;
	private int amount;
}
