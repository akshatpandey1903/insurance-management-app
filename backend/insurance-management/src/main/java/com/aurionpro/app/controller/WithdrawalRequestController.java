package com.aurionpro.app.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.WithdrawalApprovalDTO;
import com.aurionpro.app.dto.WithdrawalReportDto;
import com.aurionpro.app.dto.WithdrawalRequestDTO;
import com.aurionpro.app.dto.WithdrawalResponseDTO;
import com.aurionpro.app.service.WithdrawalRequestService;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/app/withdrawals")
@RequiredArgsConstructor
public class WithdrawalRequestController {

	@Autowired
    private WithdrawalRequestService withdrawalRequestService;

    @PostMapping("/request")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_AGENT')")
    public ResponseEntity<String> createWithdrawalRequest(@RequestBody WithdrawalRequestDTO requestDTO) {
        withdrawalRequestService.createWithdrawalRequest(requestDTO);
        return ResponseEntity.ok("Withdrawal request submitted successfully.");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<PageResponse<WithdrawalResponseDTO>> getAllRequests(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        PageResponse<WithdrawalResponseDTO> response = withdrawalRequestService.getAllWithdrawalRequests(pageNumber, pageSize);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/approve")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<String> approveOrRejectRequest(@RequestBody WithdrawalApprovalDTO approvalDTO) {
        withdrawalRequestService.approveOrRejectRequest(approvalDTO);
        return ResponseEntity.ok("Withdrawal request status updated successfully.");
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/report")
    public ResponseEntity<PageResponse<WithdrawalReportDto>> getWithdrawalReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(withdrawalRequestService.getWithdrawalReport(page, size));
    }

}
