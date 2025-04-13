package com.aurionpro.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.CustomerQueryRequestDTO;
import com.aurionpro.app.dto.CustomerQueryResponseDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.entity.CustomerQuery;
import com.aurionpro.app.entity.QueryStatus;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.CustomerQueryRepository;
import com.aurionpro.app.repository.CustomerRepository;

@Service
public class CustomerQueryServiceImpl implements CustomerQueryService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerQueryRepository queryRepository;

    @Override
    public CustomerQueryResponseDTO raiseQuery(CustomerQueryRequestDTO dto, int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Customer not found with id: " + customerId));

        CustomerQuery query = new CustomerQuery();
        query.setCustomer(customer);
        query.setSubject(dto.getSubject());
        query.setMessage(dto.getMessage());
        query.setStatus(QueryStatus.PENDING);
        query.setCreatedAt(LocalDateTime.now());
        query.setUpdatedAt(LocalDateTime.now());

        query = queryRepository.save(query);
        return toDTO(query);
    }

    @Override
    public List<CustomerQueryResponseDTO> getCustomerQueries(int customerId) {
       return queryRepository.findAllByCustomer_UserIdAndIsDeletedFalse(customerId).stream()
    		   .map(this::toDTO)
               .collect(Collectors.toList());
    }

    private CustomerQueryResponseDTO toDTO(CustomerQuery query) {
        CustomerQueryResponseDTO dto = new CustomerQueryResponseDTO();
        dto.setId(query.getId());
        dto.setSubject(query.getSubject());
        dto.setMessage(query.getMessage());
        dto.setStatus(query.getStatus().toString());
        dto.setAdminResponse(query.getAdminResponse());
        dto.setCreatedAt(query.getCreatedAt());
        dto.setUpdatedAt(query.getUpdatedAt());
        dto.setCustomerName(query.getCustomer().getFirstName() + " " + query.getCustomer().getLastName());
        return dto;
    }
    
    @Override
    public PageResponse<CustomerQueryResponseDTO> getAllQueriesPaginated(int page, int size, QueryStatus status) {
    	System.out.println("Fetching paginated queries...");
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<CustomerQuery> queryPage;
        if (status != null) {
            queryPage = queryRepository.findByStatusAndIsDeletedFalse(status, pageable);
        } else {
            queryPage = queryRepository.findAll(pageable);
        }

        List<CustomerQueryResponseDTO> dtos = queryPage.getContent().stream()
                .map(this::toDTO)
                .toList();
        
        System.out.println(dtos);
        return new PageResponse<>(
                dtos,
                queryPage.getTotalPages(),
                queryPage.getTotalElements(),
                queryPage.getSize(),
                queryPage.isLast()
        );
    }

    @Override
    public CustomerQueryResponseDTO respondToQuery(int queryId, String response) {
        CustomerQuery query = queryRepository.findById(queryId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Query not found with ID: " + queryId));

        if (query.getStatus() == QueryStatus.RESOLVED) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Query is already resolved.");
        }

        query.setAdminResponse(response);
        query.setStatus(QueryStatus.RESOLVED);
        queryRepository.save(query);

        return toDTO(query);
    }
}

