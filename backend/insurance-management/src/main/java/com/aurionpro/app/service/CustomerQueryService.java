package com.aurionpro.app.service;

import java.util.List;

import com.aurionpro.app.dto.CustomerQueryRequestDTO;
import com.aurionpro.app.dto.CustomerQueryResponseDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.entity.QueryStatus;

public interface CustomerQueryService {
    CustomerQueryResponseDTO raiseQuery(CustomerQueryRequestDTO dto, int customerId);
    List<CustomerQueryResponseDTO> getCustomerQueries(int customerId);
    
    // Admin/Employee related services
    PageResponse<CustomerQueryResponseDTO> getAllQueriesPaginated(int page, int size, QueryStatus status);
    CustomerQueryResponseDTO respondToQuery(int queryId, String response);

}

