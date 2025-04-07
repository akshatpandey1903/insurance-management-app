package com.aurionpro.app.service;

import com.aurionpro.app.dto.CustomerReportDto;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerReportServiceImpl implements CustomerReportService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public PageResponse<CustomerReportDto> getCustomerReports(Pageable pageable) {
        Page<CustomerReportDto> page = customerRepository.getCustomerReport(pageable);
        return new PageResponse<>(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.isLast()
        );
    }
}

