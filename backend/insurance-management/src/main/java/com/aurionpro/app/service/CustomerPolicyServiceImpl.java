package com.aurionpro.app.service;

import com.aurionpro.app.dto.CustomerPolicyRequestDTO;
import com.aurionpro.app.dto.CustomerPolicyResponseDTO;
import com.aurionpro.app.entity.*;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.*;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CustomerPolicyServiceImpl implements CustomerPolicyService {

    @Autowired
    private CustomerPolicyRepository customerPolicyRepository;

    @Autowired
    private InsurancePlanRepository insurancePlanRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AgentRepository agentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    public static CustomerPolicyResponseDTO toDTO(CustomerPolicy policy) {
        CustomerPolicyResponseDTO dto = new CustomerPolicyResponseDTO();
        dto.setId(policy.getId());
        dto.setCustomerName(policy.getCustomer().getFirstName() + " " + policy.getCustomer().getLastName());
        dto.setInsurancePlanName(policy.getInsurancePlan().getPlanName());
        dto.setPaymentFrequency(policy.getPaymentFrequency());
        dto.setCalculatedPremium(policy.getCalculatedPremium());
        dto.setStartDate(policy.getStartDate());
        dto.setEndDate(policy.getEndDate());
        dto.setActive(policy.isActive());
        dto.setNextDueDate(policy.getNextDueDate());
        
        dto.setAgentName(policy.getAgent() != null ? policy.getAgent().getFirstName() + " " + policy.getAgent().getLastName() : null);
        dto.setApprovedBy(policy.getApprovedBy() != null ? policy.getApprovedBy().getFirstName() + " " + policy.getApprovedBy().getLastName() : null);
        
        return dto;
    }
    
    @Override
    @Transactional
    public CustomerPolicyResponseDTO registerPolicy(CustomerPolicyRequestDTO requestDTO, int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Customer id:" + id));

        InsurancePlan plan = insurancePlanRepository.findById(requestDTO.getInsurancePlanId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Insurance Plan id:" + requestDTO.getInsurancePlanId()));

        Agent agent = null;
        if (requestDTO.getAgentId() != null) {
            agent = agentRepository.findById(requestDTO.getAgentId())
                    .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Agent id:" + requestDTO.getAgentId()));
        }

        PaymentFrequency frequency = requestDTO.getPaymentFrequency();
        BigDecimal yearlyPremium = plan.getYearlyPremiumAmount();

        BigDecimal frequencyMultiplier = switch (frequency) {
            case MONTHLY -> BigDecimal.valueOf(1.0 / 12);
            case QUARTERLY -> BigDecimal.valueOf(1.0 / 4);
            case HALF_YEARLY -> BigDecimal.valueOf(1.0 / 2);
            case YEARLY -> BigDecimal.ONE;
        };

        BigDecimal calculatedPremium = yearlyPremium.multiply(frequencyMultiplier)
                .divide(BigDecimal.valueOf(plan.getDurationYears()), 2, RoundingMode.HALF_UP);

        CustomerPolicy policy = new CustomerPolicy();
        policy.setCustomer(customer);
        policy.setInsurancePlan(plan);
        policy.setPaymentFrequency(frequency);
        policy.setCalculatedPremium(calculatedPremium);
        policy.setActive(false);
        policy.setAgent(agent);

        return toDTO(customerPolicyRepository.save(policy));
    }
    
    private LocalDate getNextDueDate(LocalDate current, PaymentFrequency frequency) {
        return switch (frequency) {
            case MONTHLY -> current.plusMonths(1);
            case QUARTERLY -> current.plusMonths(3);
            case HALF_YEARLY -> current.plusMonths(6);
            case YEARLY -> current.plusYears(1);
        };
    }

    @Override
    @Transactional
    public CustomerPolicyResponseDTO approveCustomerPolicy(int policyId, int employeeId) {
        CustomerPolicy policy = customerPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Policy not found with id: " + policyId));

        if (policy.isActive()) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Policy is already active");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Employee not found with id: " + employeeId));

        LocalDate startDate = LocalDate.now().plusDays(5);
        policy.setStartDate(startDate);
        policy.setEndDate(startDate.plusYears(policy.getInsurancePlan().getDurationYears()));
        policy.setNextDueDate(getNextDueDate(startDate, policy.getPaymentFrequency()));
        policy.setActive(true);
        policy.setApprovedBy(employee);

        return toDTO(customerPolicyRepository.save(policy));
    }
}