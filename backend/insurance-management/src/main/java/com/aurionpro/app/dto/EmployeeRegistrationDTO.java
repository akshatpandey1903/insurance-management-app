package com.aurionpro.app.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeRegistrationDTO extends AdminRegistrationDTO {
    
    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Designation is required")
    private String designation;
    
    @Column
	private boolean isActive = true;
}
