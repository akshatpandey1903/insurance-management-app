package com.aurionpro.app.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.entity.CustomerDocument;
import com.aurionpro.app.entity.DocumentType;
import com.aurionpro.app.repository.CustomerDocumentRepository;
import com.aurionpro.app.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerDocumentServiceImpl implements CustomerDocumentService{
	
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
		
	
}
