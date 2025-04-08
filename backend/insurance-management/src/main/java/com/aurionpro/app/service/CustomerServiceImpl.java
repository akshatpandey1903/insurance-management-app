package com.aurionpro.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.CustomerProfileDTO;
import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

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
}

