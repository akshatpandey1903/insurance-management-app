package com.aurionpro.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgentRegistrationDTO extends AdminRegistrationDTO {
    
    @NotBlank(message = "License number is required")
    private String licenseNumber;
}
