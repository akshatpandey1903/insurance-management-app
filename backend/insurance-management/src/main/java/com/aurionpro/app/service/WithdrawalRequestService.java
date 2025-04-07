package com.aurionpro.app.service;

import com.aurionpro.app.dto.*;

public interface WithdrawalRequestService {
    void createWithdrawalRequest(WithdrawalRequestDTO requestDTO);

    PageResponse<WithdrawalResponseDTO> getAllWithdrawalRequests(int pageNumber, int pageSize);

    void approveOrRejectRequest(WithdrawalApprovalDTO approvalDTO);
}