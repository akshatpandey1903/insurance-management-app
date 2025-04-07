package com.aurionpro.app.service;

import com.aurionpro.app.dto.CustomerReportDto;
import com.aurionpro.app.dto.PageResponse;
import org.springframework.data.domain.Pageable;

public interface CustomerReportService {
    PageResponse<CustomerReportDto> getCustomerReports(Pageable pageable);
}
