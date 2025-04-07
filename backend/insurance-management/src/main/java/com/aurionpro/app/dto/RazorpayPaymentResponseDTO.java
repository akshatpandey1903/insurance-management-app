package com.aurionpro.app.dto;

import lombok.Data;

@Data
public class RazorpayPaymentResponseDTO {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private int customerPolicyId;
}