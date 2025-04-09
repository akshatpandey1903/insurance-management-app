package com.aurionpro.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.CustomerProfileDTO;
import com.aurionpro.app.dto.CustomerRegistrationDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.UserResponseDTO;
import com.aurionpro.app.entity.Agent;
import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.entity.Role;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.AgentRepository;
import com.aurionpro.app.repository.CustomerRepository;
import com.aurionpro.app.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private AgentRepository agentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomerProfileDTO viewProfile(int customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Customer not found with id: " + customerId));

        CustomerProfileDTO dto = new CustomerProfileDTO();
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setPhone(customer.getPhoneNumber());
        dto.setAddress(customer.getAddress());
        return dto;
    }

    @Override
    @Transactional
    public CustomerProfileDTO updateProfile(int customerId, CustomerProfileDTO dto) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Customer not found with id: " + customerId));

        // Confirm current password
        if (!passwordEncoder.matches(dto.getCurrentPassword(), customer.getPassword())) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Incorrect current password.");
        }

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setPhoneNumber(dto.getPhone());
        customer.setAddress(dto.getAddress());

        customerRepository.save(customer);

        return viewProfile(customerId);
    }
    
    @Override
    public UserResponseDTO registerCustomerByAgent(CustomerRegistrationDTO dto, int agentId) {
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceNotFoundException(HttpStatus.CONFLICT, "Customer with this email already exists");
        }

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Agent not found with id: " + agentId));

        Customer customer = new Customer();
        customer.setUsername(dto.getUsername());
        customer.setEmail(dto.getEmail());
        customer.setPassword(passwordEncoder.encode(dto.getPassword()));
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setAddress(dto.getAddress());
        customer.setPhoneNumber(dto.getPhoneNumber());
        customer.setRole(roleRepo.findByRoleName("ROLE_CUSTOMER"));
        customer.setActive(dto.isActive());
        customer.setRegisteredBy(agent);
        customer.setCreatedAt(LocalDateTime.now());

        Customer saved = customerRepository.save(customer);

        return new UserResponseDTO(
                saved.getUserId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getRole().getRoleName()
        );
    }
    
    @Override
    public PageResponse<UserResponseDTO> getCustomersRegisteredByAgent(int agentId, int page, int size) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Agent not found with id: " + agentId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Customer> customers = customerRepository.findByRegisteredBy_UserId(agentId, pageable);

        List<UserResponseDTO> customerDTOs = customers.stream()
                .map(c -> new UserResponseDTO(
                        c.getUserId(),
                        c.getUsername(),
                        c.getEmail(),
                        c.getFirstName(),
                        c.getLastName(),
                        c.getRole().getRoleName()
                ))
                .toList();

        return new PageResponse<>(
        		customerDTOs,
        		customers.getTotalPages(),
        		customers.getTotalElements(),
        		customers.getSize(),
        		customers.isLast()
        );
    }
}

