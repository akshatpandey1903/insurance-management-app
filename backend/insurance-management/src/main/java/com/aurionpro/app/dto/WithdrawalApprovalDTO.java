package com.aurionpro.app.dto;

import com.aurionpro.app.entity.WithdrawalStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalApprovalDTO {
	
	@NotNull(message = "Required field")
    private int withdrawalId;
	
	@NotNull(message = "Required field")
    private int approvedById;
	
	@NotNull(message = "Required field")
    private WithdrawalStatus status;
	
	@NotBlank(message = "Required field")
    private String remarks;
}
