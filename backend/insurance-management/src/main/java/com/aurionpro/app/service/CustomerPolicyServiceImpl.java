package com.aurionpro.app.service;

import com.aurionpro.app.dto.CustomerPolicyRequestDTO;
import com.aurionpro.app.dto.CustomerPolicyResponseDTO;
import com.aurionpro.app.entity.*;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.*;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
    private TransactionRepository transactionRepository;
    
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

        BigDecimal calculatedPremium = yearlyPremium.multiply(frequencyMultiplier).setScale(2, RoundingMode.HALF_UP);

        // Build CustomerPolicy
        CustomerPolicy policy = new CustomerPolicy();
        policy.setCustomer(customer);
        policy.setInsurancePlan(plan);
        policy.setPaymentFrequency(frequency);
        policy.setCalculatedPremium(calculatedPremium);
        policy.setStartDate(requestDTO.getStartDate());
        policy.setEndDate(requestDTO.getStartDate().plusYears(plan.getDurationYears()));
        policy.setActive(true);
        policy.setAgent(agent);

        CustomerPolicy savedPolicy = customerPolicyRepository.save(policy);

        // Update agent's commission
        if (agent != null) {
            BigDecimal commissionRate = BigDecimal.valueOf(plan.getCommissionRate()); // e.g., 0.1 for 10%
            BigDecimal commissionAmount = calculatedPremium.multiply(commissionRate).setScale(2, RoundingMode.HALF_UP);
            agent.setTotalEarnings(agent.getTotalEarnings().add(commissionAmount));
            agentRepository.save(agent);

            Transaction agentCommissionTxn = new Transaction();
            agentCommissionTxn.setAmount(commissionAmount);
            agentCommissionTxn.setDescription("Commission earned for policy sale");
            agentCommissionTxn.setTransactionType(TransactionType.COMMISSION);
            agentCommissionTxn.setUser(agent);
            agentCommissionTxn.setUserRole("AGENT");
            agentCommissionTxn.setCustomerPolicy(savedPolicy);
            transactionRepository.save(agentCommissionTxn);
        }

        Transaction customerTxn = new Transaction();
        customerTxn.setAmount(calculatedPremium);
        customerTxn.setDescription("Premium payment for policy");
        customerTxn.setTransactionType(TransactionType.POLICY_PURCHASE);
        customerTxn.setUser(customer);
        customerTxn.setUserRole("CUSTOMER");
        customerTxn.setCustomerPolicy(savedPolicy);
        transactionRepository.save(customerTxn);

        return toDTO(savedPolicy);
    }
}