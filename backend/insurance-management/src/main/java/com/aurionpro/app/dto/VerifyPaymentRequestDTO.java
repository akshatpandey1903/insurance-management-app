package com.aurionpro.app.dto;

import lombok.Data;

@Data
public class VerifyPaymentRequestDTO {
	private int customerPolicyId;
	private String razorpayOrderId;
	private String razorpayPaymentId;
	private String razorpaySignature;
}
