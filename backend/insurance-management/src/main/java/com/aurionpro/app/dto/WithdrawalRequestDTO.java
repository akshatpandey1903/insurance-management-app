package com.aurionpro.app.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalRequestDTO {
	
	@NotNull(message = "Amount is required")
    private BigDecimal amount;
	
	@NotNull(message = "Required field")
    private int requestedById;
	
	@NotBlank(message = "Required field")
    private String userRole;    
}
