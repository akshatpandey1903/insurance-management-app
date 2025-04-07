package com.aurionpro.app.service;

import com.aurionpro.app.dto.*;
import com.aurionpro.app.entity.*;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WithdrawalRequestServiceImpl implements WithdrawalRequestService {

    private final WithdrawalRequestRepository withdrawalRepo;
    private final UserRepository userRepo;
    private final EmployeeRepository employeeRepo;

    @Override
    public void createWithdrawalRequest(WithdrawalRequestDTO requestDTO) {
        User user = userRepo.findById(requestDTO.getRequestedById())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"User not found"));

        WithdrawalRequest request = new WithdrawalRequest();
        request.setAmount(requestDTO.getAmount());
        request.setRequestedBy(user);
        request.setStatus(WithdrawalStatus.PENDING);
        request.setRequestedAt(LocalDateTime.now());

        withdrawalRepo.save(request);
    }

    @Override
    public PageResponse<WithdrawalResponseDTO> getAllWithdrawalRequests(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("requestedAt").descending());
        Page<WithdrawalRequest> requests = withdrawalRepo.findAll(pageable);

        return new PageResponse<>(
            requests.getContent().stream().map(this::mapToDTO).collect(Collectors.toList()),
            requests.getTotalPages(),
            requests.getTotalElements(),
            requests.getSize(),
            requests.isLast()
        );
    }

    @Override
    public void approveOrRejectRequest(WithdrawalApprovalDTO approvalDTO) {
        WithdrawalRequest request = withdrawalRepo.findById(approvalDTO.getWithdrawalId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Withdrawal request not found"));

        if (request.getStatus() != WithdrawalStatus.PENDING) {
            throw new IllegalStateException("Request already processed");
        }

        Employee approver = employeeRepo.findById(approvalDTO.getApprovedById())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Approver not found"));

        request.setStatus(approvalDTO.getStatus());
        request.setApprovedBy(approver);
        request.setApprovedAt(LocalDateTime.now());
        request.setRemarks(approvalDTO.getRemarks());

        withdrawalRepo.save(request);
    }

    private WithdrawalResponseDTO mapToDTO(WithdrawalRequest request) {
        return new WithdrawalResponseDTO(
            request.getWithdrawalId(),
            request.getAmount(),
            request.getRequestedBy().getRole().getRoleName(),
            request.getRequestedBy().getUserId(),
            request.getRequestedBy().getFirstName() + " " + request.getRequestedBy().getLastName(),
            request.getStatus(),
            request.getRequestedAt(),
            request.getApprovedBy() != null ? request.getApprovedBy().getUserId() : null,
            request.getApprovedBy() != null ? request.getApprovedBy().getFirstName() + " " + request.getApprovedBy().getLastName() : null,
            request.getApprovedAt(),
            request.getRemarks()
        );
    }
}
