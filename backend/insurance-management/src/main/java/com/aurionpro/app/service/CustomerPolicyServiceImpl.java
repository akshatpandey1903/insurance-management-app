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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CustomerPolicyServiceImpl implements CustomerPolicyService {
	
	@Autowired
	private CustomerDocumentRepository customerDocumentRepository;

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
        
        validateRequiredDocuments(customer, plan);
        
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

        CustomerPolicy savedPolicy = customerPolicyRepository.save(policy);

        Agent agent = policy.getAgent();
        if (agent != null) {
        	
            BigDecimal commission = policy.getInsurancePlan()
                    .getYearlyPremiumAmount()
                    .multiply(BigDecimal.valueOf(policy.getInsurancePlan().getCommissionRate()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            BigDecimal currentEarnings = agent.getTotalEarnings() != null ? agent.getTotalEarnings() : BigDecimal.ZERO;
            agent.setTotalEarnings(currentEarnings.add(commission));

            if (agent.getSoldPolicies() == null) {
                agent.setSoldPolicies(new java.util.ArrayList<>());
            }
            agent.getSoldPolicies().add(savedPolicy);

            agentRepository.save(agent);
        }

        return toDTO(savedPolicy);
    }
    
    @Override
    @Transactional
    public CustomerPolicyResponseDTO cancelPolicy(int customerId, int policyId) {
        CustomerPolicy policy = customerPolicyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Policy not found with ID: " + policyId));

        if (policy.getCustomer().getUserId() != customerId) {
            throw new ResourceNotFoundException(HttpStatus.FORBIDDEN, "You are not authorized to cancel this policy.");
        }

        if (!policy.isActive()) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Policy is not active or already cancelled.");
        }

        // Cancel the policy
        policy.setActive(false);
        policy.setCancelled(true);
        policy.setCancellationDate(LocalDate.now());

        customerPolicyRepository.save(policy);

        return toDTO(policy);
    }
    
    private void validateRequiredDocuments(Customer customer, InsurancePlan plan) {
        System.out.println("Called validateRequiredDocuments for customer: " + customer.getUserId());

        List<InsurancePlanDocument> requiredDocs = plan.getRequiredDocuments();

        if (requiredDocs == null || requiredDocs.isEmpty()) {
            System.out.println("No required documents specified for this plan.");
            return;
        }

        List<DocumentType> requiredTypes = requiredDocs.stream()
                .map(InsurancePlanDocument::getDocumentType)
                .toList();

        List<CustomerDocument> approvedDocs = customerDocumentRepository
                .findByCustomerAndStatus(customer, DocumentStatus.APPROVED);

        System.out.println("Approved docs found: " + approvedDocs.size());
        approvedDocs.forEach(doc -> System.out.println("Approved: " + doc.getDocumentType()));

        Set<DocumentType> approvedTypes = approvedDocs.stream()
                .map(CustomerDocument::getDocumentType)
                .collect(Collectors.toSet());

        List<DocumentType> missing = requiredTypes.stream()
                .filter(type -> !approvedTypes.contains(type))
                .toList();

        if (!missing.isEmpty()) {
            String missingDocs = missing.stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST, "Missing approved documents: " + missingDocs);
        }
    }
    
}