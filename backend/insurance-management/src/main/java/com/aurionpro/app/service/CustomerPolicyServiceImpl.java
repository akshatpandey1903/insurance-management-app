package com.aurionpro.app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.CustomerPolicyRequestDTO;
import com.aurionpro.app.dto.CustomerPolicyResponseDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.dto.PurchasedPolicyDto;
import com.aurionpro.app.entity.Agent;
import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.entity.CustomerDocument;
import com.aurionpro.app.entity.CustomerPolicy;
import com.aurionpro.app.entity.DocumentStatus;
import com.aurionpro.app.entity.DocumentType;
import com.aurionpro.app.entity.Employee;
import com.aurionpro.app.entity.InsurancePlan;
import com.aurionpro.app.entity.InsurancePlanDocument;
import com.aurionpro.app.entity.PaymentFrequency;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.AgentRepository;
import com.aurionpro.app.repository.CustomerDocumentRepository;
import com.aurionpro.app.repository.CustomerPolicyRepository;
import com.aurionpro.app.repository.CustomerRepository;
import com.aurionpro.app.repository.EmployeeRepository;
import com.aurionpro.app.repository.InsurancePlanRepository;

import jakarta.transaction.Transactional;

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
    
    @Autowired
    private ModelMapper modelMapper;
    
    public static CustomerPolicyResponseDTO toDTO(CustomerPolicy policy) {
        CustomerPolicyResponseDTO dto = new CustomerPolicyResponseDTO();
        dto.setId(policy.getId());
        dto.setCustomerName(policy.getCustomer().getFirstName() + " " + policy.getCustomer().getLastName());
        dto.setInsurancePlanName(policy.getInsurancePlan().getPlanName());
        dto.setPaymentFrequency(policy.getPaymentFrequency());
        dto.setCalculatedPremium(policy.getCalculatedPremium());
        dto.setSelectedCoverageAmount(policy.getSelectedCoverageAmount());
        dto.setSelectedDurationYears(policy.getSelectedDurationYears());
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
    public CustomerPolicyResponseDTO registerPolicy(CustomerPolicyRequestDTO requestDTO, int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Customer id: " + customerId));

        InsurancePlan plan = insurancePlanRepository.findById(requestDTO.getInsurancePlanId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Insurance Plan id: " + requestDTO.getInsurancePlanId()));

        validateRequiredDocuments(customer, plan);

        BigDecimal coverage = requestDTO.getSelectedCoverageAmount();
        int duration = requestDTO.getSelectedDurationYears();

        if (coverage.compareTo(plan.getMinCoverageAmount()) < 0 || coverage.compareTo(plan.getMaxCoverageAmount()) > 0) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST,
                    "Coverage amount must be between " + plan.getMinCoverageAmount() + " and " + plan.getMaxCoverageAmount());
        }

        if (duration < plan.getMinDurationYears() || duration > plan.getMaxDurationYears()) {
            throw new ResourceNotFoundException(HttpStatus.BAD_REQUEST,
                    "Duration must be between " + plan.getMinDurationYears() + " and " + plan.getMaxDurationYears());
        }

        Agent agent = null;
        if (requestDTO.getLicenseNumber() != null) {
            agent = agentRepository.findByLicenseNumberAndIsActiveTrue(requestDTO.getLicenseNumber())
                    .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Agent license No: " + requestDTO.getLicenseNumber()));
        }

        PaymentFrequency frequency = requestDTO.getPaymentFrequency();

        BigDecimal basePremium = coverage
                .divide(BigDecimal.valueOf(1000), 4, RoundingMode.HALF_UP)
                .multiply(plan.getPremiumRatePerThousandPerYear());
        
        BigDecimal frequencyFactor = switch (frequency) {
            case MONTHLY -> BigDecimal.valueOf(1.0 / (12 * duration));
            case QUARTERLY -> BigDecimal.valueOf(1.0 / (4 * duration));
            case HALF_YEARLY -> BigDecimal.valueOf(1.0 / (2 * duration));
            case YEARLY -> BigDecimal.valueOf(1.0 / duration);
        };

        BigDecimal calculatedPremium = basePremium.multiply(frequencyFactor)
                .setScale(2, RoundingMode.HALF_UP);

        CustomerPolicy policy = new CustomerPolicy();
        policy.setCustomer(customer);
        policy.setInsurancePlan(plan);
        policy.setPaymentFrequency(frequency);
        policy.setCalculatedPremium(calculatedPremium);
        policy.setSelectedCoverageAmount(coverage);
        policy.setSelectedDurationYears(duration);
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
        int durationYears = policy.getSelectedDurationYears();

        policy.setStartDate(startDate);
        policy.setEndDate(startDate.plusYears(durationYears));
        policy.setNextDueDate(getNextDueDate(startDate, policy.getPaymentFrequency()));
        policy.setActive(true);
        policy.setApprovedBy(employee);

        CustomerPolicy savedPolicy = customerPolicyRepository.save(policy);

        Agent agent = policy.getAgent();
        if (agent != null) {
            BigDecimal yearlyPremium;

            switch (policy.getPaymentFrequency()) {
                case MONTHLY -> yearlyPremium = policy.getCalculatedPremium().multiply(BigDecimal.valueOf(12));
                case QUARTERLY -> yearlyPremium = policy.getCalculatedPremium().multiply(BigDecimal.valueOf(4));
                case HALF_YEARLY -> yearlyPremium = policy.getCalculatedPremium().multiply(BigDecimal.valueOf(2));
                case YEARLY -> yearlyPremium = policy.getCalculatedPremium();
                default -> yearlyPremium = BigDecimal.ZERO;
            }

            BigDecimal commission = yearlyPremium
                    .multiply(BigDecimal.valueOf(policy.getInsurancePlan().getCommissionRate()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            BigDecimal currentEarnings = agent.getTotalEarnings() != null ? agent.getTotalEarnings() : BigDecimal.ZERO;
            agent.setTotalEarnings(currentEarnings.add(commission));

            if (agent.getSoldPolicies() == null) {
                agent.setSoldPolicies(new ArrayList<>());
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
    
    @Override
    public PageResponse<CustomerPolicyResponseDTO> getUnapprovedPolicies(int page, int size) {
        Page<CustomerPolicy> policies = customerPolicyRepository.findByIsActiveFalseAndApprovedByNullAndIsRejectedFalse(PageRequest.of(page, size));

        List<CustomerPolicyResponseDTO> dtoList = policies.getContent().stream()
            .map(policy -> {
                String customerName = policy.getCustomer().getFirstName() + " " + policy.getCustomer().getLastName();

                String agentName = policy.getAgent() != null
                    ? policy.getAgent().getFirstName() + " " + policy.getAgent().getLastName()
                    : "N/A";

                return new CustomerPolicyResponseDTO(
                    policy.getId(),
                    customerName,
                    policy.getInsurancePlan().getPlanName(),
                    policy.getPaymentFrequency(),
                    policy.getCalculatedPremium(),
                    policy.getSelectedCoverageAmount(),
                    policy.getSelectedDurationYears(),
                    null, // Start date (not yet approved)
                    null, // End date (not yet approved)
                    null, // Next due date (not yet approved)
                    false, // Active status (not approved)
                    null, // Approved by (not yet approved)
                    agentName
                );
            })
            .collect(Collectors.toList());

        return new PageResponse<>(dtoList, policies.getTotalPages(), policies.getTotalElements(), size, policies.isLast());
    }
    
    @Override
    public void rejectCustomerPolicy(int policyId, int employeeId, String reason) {
        CustomerPolicy policy = customerPolicyRepository.findById(policyId)
            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Policy not found"));

        if (policy.isActive()) {
            throw new IllegalStateException("Policy is already approved");
        }

        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Employee not found"));

        policy.setRejected(true);
        policy.setActive(false);
        policy.setApprovedBy(employee);
        policy.setStartDate(null);
        policy.setEndDate(null);
        policy.setRejectionReason(reason);

        customerPolicyRepository.save(policy);
    }
    
    @Override
    public List<PurchasedPolicyDto> getPurchasedPolicies(int customerId) {
        List<CustomerPolicy> policies = customerPolicyRepository.findByCustomerUserIdAndIsCancelledFalse(customerId);

        return policies.stream()
                .map(policy -> {
                    PurchasedPolicyDto dto = new PurchasedPolicyDto();
                    dto.setPolicyId(policy.getId());
                    dto.setPlanName(policy.getInsurancePlan().getPlanName());
                    dto.setPaymentFrequency(policy.getPaymentFrequency());
                    dto.setCalculatedPremium(policy.getCalculatedPremium());
                    dto.setSelectedCoverageAmount(policy.getSelectedCoverageAmount());
                    dto.setSelectedDurationYears(policy.getSelectedDurationYears());
                    dto.setActive(policy.isActive());
                    dto.setStartDate(policy.getStartDate());
                    dto.setEndDate(policy.getEndDate());
                    dto.setNextDueDate(policy.getNextDueDate());
                    if(policy.getApprovedBy() != null) {
                    	dto.setApprovedBy(policy.getApprovedBy().getFirstName()+" "+policy.getApprovedBy().getLastName());
                    }           

                    return dto;
                })
                .collect(Collectors.toList());
    }

}