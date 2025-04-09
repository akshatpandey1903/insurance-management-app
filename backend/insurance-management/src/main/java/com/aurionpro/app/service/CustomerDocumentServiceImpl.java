package com.aurionpro.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aurionpro.app.dto.CustomerDocumentResponseDTO;
import com.aurionpro.app.dto.DocumentStatusUpdateRequestDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.entity.CustomerDocument;
import com.aurionpro.app.entity.DocumentStatus;
import com.aurionpro.app.entity.DocumentType;
import com.aurionpro.app.entity.Employee;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.CustomerDocumentRepository;
import com.aurionpro.app.repository.CustomerRepository;
import com.aurionpro.app.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerDocumentServiceImpl implements CustomerDocumentService{
	
	@Autowired
	private final EmployeeRepository employeeRepository;
	
	@Autowired
	private final CustomerDocumentRepository documentRepository;
	
	@Autowired
    private final CustomerRepository customerRepository;
	
	@Autowired
    private final CloudinaryService cloudinaryService;

	@Override
	public String uploadDocument(int customerId, DocumentType documentType, MultipartFile file) {
		Optional<Customer> customerOpt = customerRepository.findById(customerId);
		if(customerOpt.isEmpty()) {
			throw new RuntimeException("Customer not found");
		}
		
		Customer customer = customerOpt.get();
		
		String documentUrl = cloudinaryService.uploadFile(file);
		
		CustomerDocument document = new CustomerDocument();
		document.setCustomer(customer);
		document.setDocumentType(documentType);
		document.setDocumentUrl(documentUrl);
		document.setUploadedAt(LocalDateTime.now());
		
		documentRepository.save(document);
		return documentUrl;
	}
	
	@Override
	public PageResponse<CustomerDocumentResponseDTO> getPendingDocuments(int page, int size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by("uploadedAt").descending());
	    Page<CustomerDocument> pageResult = documentRepository.findByStatusAndIsDeletedFalse(DocumentStatus.PENDING, pageable);

	    List<CustomerDocumentResponseDTO> content = pageResult.getContent().stream()
	            .map(this::mapToResponseDTO)
	            .toList();

	    return new PageResponse<>(
	            content,
	            pageResult.getTotalPages(),
	            pageResult.getTotalElements(),
	            pageResult.getSize(),
	            pageResult.isLast()
	    );
	}

	
	@Override
	public void updateDocumentStatus(DocumentStatusUpdateRequestDTO request, int employeeId) {
	    CustomerDocument document = documentRepository.findById(request.getDocumentId())
	            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Document not found"));

	    if (document.getStatus() != DocumentStatus.PENDING) {
	        throw new RuntimeException("Document already reviewed.");
	    }

	    Employee employee = employeeRepository.findById(employeeId)
	            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Employee not found"));

	    document.setStatus(request.getStatus());
	    document.setVerifiedBy(employee);
	    document.setVerifiedAt(LocalDateTime.now());

	    if (request.getStatus() == DocumentStatus.REJECTED) {
	        document.setRejectionReason(request.getRejectionReason());
	    }

	    documentRepository.save(document);
	}
	
	private CustomerDocumentResponseDTO mapToResponseDTO(CustomerDocument document) {
	    return new CustomerDocumentResponseDTO(
	        document.getDocumentId(),
	        document.getCustomer().getUserId(),
	        document.getCustomer().getFirstName() + " " + document.getCustomer().getLastName(),
	        document.getDocumentType(),
	        document.getDocumentUrl(),
	        document.getStatus(),
	        document.getUploadedAt()
	    );
	}
	
	@Override
    public List<CustomerDocumentResponseDTO> getDocumentsByCustomerId(int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Customer not found with ID: " + customerId));

        List<CustomerDocument> documents = documentRepository.findByCustomerUserId(customer.getUserId());

        return documents.stream().map(doc -> {
            CustomerDocumentResponseDTO dto = new CustomerDocumentResponseDTO();
            dto.setDocumentId(doc.getDocumentId());
            dto.setCustomerId(customer.getUserId());
            dto.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
            dto.setDocumentType(doc.getDocumentType());
            dto.setDocumentUrl(doc.getDocumentUrl());
            dto.setDocumentStatus(doc.getStatus());
            dto.setUploadedAt(doc.getUploadedAt());
            return dto;
        }).toList();
    }

}
