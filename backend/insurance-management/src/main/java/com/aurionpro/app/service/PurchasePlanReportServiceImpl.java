package com.aurionpro.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.PlanPurchaseReportDto;
import com.aurionpro.app.repository.CustomerPolicyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchasePlanReportServiceImpl implements PurchasePlanReportService{
	
	@Autowired
	private CustomerPolicyRepository customerPolicyRepository;
	
	@Override
    public PageResponse<PlanPurchaseReportDto> getPlanPurchaseReport(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PlanPurchaseReportDto> reportPage = customerPolicyRepository.fetchPolicyPaymentReport(pageable);

        return new PageResponse<>(
        		reportPage.getContent(),
        		reportPage.getTotalPages(),
        		reportPage.getTotalElements(),
        		reportPage.getSize(),
        		reportPage.isLast()
        );
    }
}
