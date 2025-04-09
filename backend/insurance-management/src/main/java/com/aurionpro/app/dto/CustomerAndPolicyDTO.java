package com.aurionpro.app.dto;

import lombok.Data;

@Data
public class CustomerAndPolicyDTO {
    private CustomerRegistrationDTO customerDTO;
    private CustomerPolicyRequestDTO policyDTO;
}
