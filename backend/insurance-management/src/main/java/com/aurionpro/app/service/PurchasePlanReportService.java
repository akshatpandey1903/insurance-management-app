package com.aurionpro.app.service;

import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.PlanPurchaseReportDto;

public interface PurchasePlanReportService {
	PageResponse<PlanPurchaseReportDto> getPlanPurchaseReport(int page, int size);
}
