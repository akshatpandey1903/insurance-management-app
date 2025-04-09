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
import com.aurionpro.app.dto.DocumentApprovalRequestDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.entity.CustomerDocument;
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
	public PageResponse<CustomerDocumentResponseDTO> getPendingDocuments(int pageNumber, int pageSize) {
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("uploadedAt").descending());
	    Page<CustomerDocument> page = documentRepository.findByIsVerifiedFalseAndIsDeletedFalse(pageable);

	    List<CustomerDocumentResponseDTO> content = page.getContent().stream().map(doc -> {
	        CustomerDocumentResponseDTO dto = new CustomerDocumentResponseDTO();
	        dto.setDocumentId(doc.getDocumentId());
	        dto.setCustomerId(doc.getCustomer().getUserId());
	        dto.setCustomerName(doc.getCustomer().getFirstName() + doc.getCustomer().getLastName());
	        dto.setDocumentType(doc.getDocumentType());
	        dto.setDocumentUrl(doc.getDocumentUrl());
	        dto.setVerified(doc.isVerified());
	        dto.setUploadedAt(doc.getUploadedAt());
	        return dto;
	    }).toList();

	    return new PageResponse<>(
                content,
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.isLast()
        );
	}
	
	@Override
	public void approveDocument(DocumentApprovalRequestDTO request, int employeeId) {
	    CustomerDocument document = documentRepository.findById(request.getDocumentId())
	            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Document not found"));

	    if (document.isVerified()) {
	        throw new RuntimeException("Document already verified.");
	    }

	    Employee employee = employeeRepository.findById(employeeId)
	            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Employee not found"));

	    document.setVerifiedBy(employee);
	    document.setVerifiedAt(LocalDateTime.now());
	    document.setVerified(true);

	    documentRepository.save(document);
	}

}
