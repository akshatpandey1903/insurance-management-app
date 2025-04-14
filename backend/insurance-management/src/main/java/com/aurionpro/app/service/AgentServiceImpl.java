package com.aurionpro.app.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.AgentAssignedPolicyDTO;
import com.aurionpro.app.dto.AgentCommissionReportDto;
import com.aurionpro.app.dto.AgentEarningsDTO;
import com.aurionpro.app.dto.AgentProfileDTO;
import com.aurionpro.app.dto.AgentResponseDTO;
import com.aurionpro.app.dto.AgentUpdateRequestDTO;
import com.aurionpro.app.dto.PageResponse;
import com.aurionpro.app.entity.Agent;
import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.entity.CustomerPolicy;
import com.aurionpro.app.entity.Employee;
import com.aurionpro.app.entity.InsurancePlan;
import com.aurionpro.app.entity.User;
import com.aurionpro.app.exceptions.ResourceNotFoundException;
import com.aurionpro.app.repository.AgentRepository;
import com.aurionpro.app.repository.CustomerPolicyRepository;
import com.aurionpro.app.repository.EmployeeRepository;
import com.aurionpro.app.repository.UserRepository;

@Service
public class AgentServiceImpl implements AgentService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
    private AgentRepository agentRepository;
	
	@Autowired
    private EmployeeRepository employeeRepository;
	
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomerPolicyRepository customerPolicyRepository;
	
	public AgentServiceImpl() {
		this.modelMapper = new ModelMapper();
	}

	@Override
	public PageResponse<AgentResponseDTO> getPendingAgents(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
	    Page<Agent> page = agentRepository.findByIsApprovedFalseAndIsActiveTrue(pageable);

	    List<AgentResponseDTO> content = page.getContent()
	        .stream()
	        .map(agent -> new AgentResponseDTO(
	            agent.getUserId(),
	            agent.getFirstName() + " "  + agent.getLastName(),
	            agent.getUsername(),
	            agent.getEmail(),
	            agent.getLicenseNumber(),
	            agent.isApproved()
	        ))
	        .toList();

	    return new PageResponse<>(
	        content,
	        page.getTotalPages(),
	        page.getTotalElements(),
	        page.getSize(),
	        page.isLast()
	    );
	}

	@Override
	public AgentResponseDTO approveAgent(int agentId, int approverId) {
		Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Agent not found"));
		
		if(!agent.isActive()) {
			throw new IllegalStateException("Agent is inactive");
		}
		
        if (agent.isApproved()) {
            throw new IllegalStateException("Agent is already approved");
        }

        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Approver not found"));

        agent.setApproved(true);
        agent.setApprovedBy(approver);

        Agent updated = agentRepository.save(agent);
        
        String htmlContent = getAgentApprovalEmail(updated.getFirstName() + " " + updated.getLastName());
        emailService.sendHtmlEmail(updated.getEmail(), "Your ClaimSureLIC Agent Application is Approved", htmlContent);
        
        AgentResponseDTO response = new AgentResponseDTO(
        		updated.getUserId(),
        		updated.getFirstName() + " "  + agent.getLastName(),
        		updated.getUsername(),
        		updated.getEmail(),
        		updated.getLicenseNumber(),
        		updated.isApproved()
        		);
        return response;
	}
	
	@Override
	public AgentResponseDTO rejectAgent(int agentId, int approverId) {
		Agent agent = agentRepository.findById(agentId)
		        .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Agent not found"));
		agent.setActive(false);;
		Agent updated = agentRepository.save(agent);
		
		String htmlContent = getAgentRejectionEmail(updated.getFirstName() + " " + updated.getLastName());
	    emailService.sendHtmlEmail(updated.getEmail(), "Update on Your ClaimSureLIC Agent Application", htmlContent);
	    
		AgentResponseDTO response = new AgentResponseDTO(
				updated.getUserId(),
				updated.getFirstName() + " "  + agent.getLastName(),
				updated.getUsername(),
				updated.getEmail(),
	        	updated.getLicenseNumber(),
	       		updated.isApproved()
		);
		
		return response;
	}

	@Override
	public void softDeleteAgent(int id) {
		Agent agent = agentRepository.findById(id)
		        .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Agent not found"));
		    agent.setActive(false);;
		    agentRepository.save(agent);
	}
	


	@Override
	public List<AgentAssignedPolicyDTO> getAssignedPolicies(int agentId) {
	    Agent agent = agentRepository.findById(agentId)
	            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Agent not found with ID: " + agentId));

	    List<CustomerPolicy> policies = customerPolicyRepository.findByAgentAndIsActiveTrue(agent);

	    return policies.stream().map(policy -> {
	        AgentAssignedPolicyDTO dto = new AgentAssignedPolicyDTO();
	        dto.setCustomerName(policy.getCustomer().getFirstName() + " " + policy.getCustomer().getLastName());
	        dto.setInsurancePlanName(policy.getInsurancePlan().getPlanName());
	        dto.setStartDate(policy.getStartDate());
	        dto.setEndDate(policy.getEndDate());
	        dto.setPremiumAmount(policy.getCalculatedPremium());

	        BigDecimal commissionRate = BigDecimal.valueOf(policy.getInsurancePlan().getCommissionRate());
	        BigDecimal commission = policy.getCalculatedPremium().multiply(commissionRate).divide(BigDecimal.valueOf(100));

	        dto.setCommissionAmount(commission);

	        return dto;
	    }).toList();
	}
	
	@Override
	public AgentProfileDTO getProfile(int agentId) {
	    Agent agent = agentRepository.findById(agentId)
	        .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "Agent not found with ID: " + agentId));

	    String approvedBy = agent.getApprovedBy() != null
	        ? agent.getApprovedBy().getFirstName() + " " + agent.getApprovedBy().getLastName()
	        : null;

	    return new AgentProfileDTO(
	        agent.getUserId(),
	        agent.getUsername(),
	        agent.getEmail(),
	        agent.getFirstName(),
	        agent.getLastName(),
	        agent.isActive(),
	        agent.getLicenseNumber(),
	        agent.isApproved(),
	        approvedBy,
	        agent.getTotalEarnings(),
	        agent.getCreatedAt()
	    );
	}
	
	@Override
	public AgentCommissionReportDto getCommissionReport(int agentId) {
	    Agent agent = agentRepository.findById(agentId)
	            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Agent not found"));

	    List<CustomerPolicy> policies = customerPolicyRepository.findAllByAgentLicense(agent.getLicenseNumber());

	    long totalPolicies = policies.size();
	    BigDecimal totalEarnings = BigDecimal.ZERO;
	    BigDecimal totalCommissionRate = BigDecimal.ZERO;

	    for (CustomerPolicy policy : policies) {
	        InsurancePlan plan = policy.getInsurancePlan();
	        BigDecimal premium = policy.getCalculatedPremium();
	        double commissionRate = plan.getCommissionRate();

	        BigDecimal commissionEarned = premium.multiply(BigDecimal.valueOf(commissionRate / 100.0));
	        totalEarnings = totalEarnings.add(commissionEarned);
	        totalCommissionRate = totalCommissionRate.add(BigDecimal.valueOf(commissionRate));
	    }

	    BigDecimal averageCommissionRate = totalPolicies > 0
	            ? totalCommissionRate.divide(BigDecimal.valueOf(totalPolicies), 2, RoundingMode.HALF_UP)
	            : BigDecimal.ZERO;

	    return new AgentCommissionReportDto(
	            agent.getUserId(),
	            agent.getFirstName(),
	            agent.getLastName(),
	            agent.getEmail(),
	            totalPolicies,
	            averageCommissionRate,
	            totalEarnings
	    );
	}
	
	@Override
	public List<AgentEarningsDTO> getEarningsDetails(int agentId) {
	    Agent agent = agentRepository.findById(agentId)
	            .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Agent not found"));

	    List<CustomerPolicy> policies = customerPolicyRepository.findAllByAgentLicense(agent.getLicenseNumber());

	    return policies.stream().map(policy -> {
	        Customer customer = policy.getCustomer();
	        InsurancePlan plan = policy.getInsurancePlan();
	        BigDecimal premium = policy.getCalculatedPremium();
	        double commissionRate = plan.getCommissionRate();
	        BigDecimal commissionEarned = premium.multiply(BigDecimal.valueOf(commissionRate / 100.0));
	        
	        return new AgentEarningsDTO(
	                customer.getFirstName() + " " + customer.getLastName(),
	                plan.getPlanName(),
	                premium,
	                commissionRate,
	                commissionEarned,
	                policy.getStartDate()
	        );
	    }).collect(Collectors.toList());
	}
	
	
	@Override
	public AgentResponseDTO getAgentProfile(int agentId) {
	    Agent agent = agentRepository.findById(agentId)
	        .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Agent not found"));
	    
	    return new AgentResponseDTO(
	        agent.getUserId(),
	        agent.getFirstName() + " " + agent.getLastName(),
	        agent.getUsername(),
	        agent.getEmail(),
	        agent.getLicenseNumber(),
	        agent.isApproved()
	    );
	}

	@Override
	public AgentResponseDTO updateAgentProfile(int agentId, AgentUpdateRequestDTO dto) {
	    Agent agent = agentRepository.findById(agentId)
	        .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Agent not found"));

	    User user = userRepository.findById(agentId)
	    	.orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND ,"Agent not found"));
	    
	    if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
	    if (dto.getLastName() != null) user.setLastName(dto.getLastName());
	    if (dto.getUsername() != null) user.setUsername(dto.getUsername());
	    if (dto.getEmail() != null) user.setEmail(dto.getEmail());
	    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
	        user.setPassword(passwordEncoder.encode(dto.getPassword()));
	    }

	    userRepository.save(user);

	    return new AgentResponseDTO(
	        user.getUserId(),
	        user.getFirstName() + " " + user.getLastName(),
	        user.getUsername(),
	        user.getEmail(),
	        agent.getLicenseNumber(),
	        agent.isApproved()
	    );
	}


	
	public String getAgentApprovalEmail(String name) {
	    return """
	    <!DOCTYPE html>
	    <html>
	    <head>
	      <style>
	        .email-container {
	          max-width: 600px;
	          margin: 0 auto;
	          font-family: 'Segoe UI', Arial, sans-serif;
	          color: #333333;
	          line-height: 1.6;
	        }
	        
	        .email-header {
	          background-color: #1E5C97;
	          color: white;
	          padding: 20px;
	          text-align: center;
	          border-radius: 5px 5px 0 0;
	        }
	        
	        .email-content {
	          padding: 30px;
	          background-color: #ffffff;
	          border-left: 1px solid #e5e5e5;
	          border-right: 1px solid #e5e5e5;
	        }
	        
	        .email-footer {
	          background-color: #f5f5f5;
	          padding: 20px;
	          text-align: center;
	          font-size: 14px;
	          color: #666666;
	          border-radius: 0 0 5px 5px;
	          border: 1px solid #e5e5e5;
	        }
	        
	        .greeting {
	          font-size: 22px;
	          font-weight: 600;
	          margin-bottom: 20px;
	          color: #1E5C97;
	        }
	        
	        .role-highlight {
	          background-color: #F0F7FF;
	          border-left: 4px solid #1E5C97;
	          padding: 10px 15px;
	          margin: 15px 0;
	        }
	        
	        .success-highlight {
	          background-color: #EEFBF5;
	          border-left: 4px solid #34A853;
	          padding: 10px 15px;
	          margin: 15px 0;
	        }
	        
	        .cta-button {
	          background-color: #1E5C97;
	          color: white;
	          padding: 12px 25px;
	          text-decoration: none;
	          border-radius: 4px;
	          display: inline-block;
	          margin: 15px 0;
	          font-weight: 500;
	        }
	        
	        .divider {
	          border-top: 1px solid #eeeeee;
	          margin: 20px 0;
	        }
	        
	        .logo {
	          font-size: 24px;
	          font-weight: 700;
	          color: white;
	          text-decoration: none;
	        }
	      </style>
	    </head>
	    <body style="background-color: #f9f9f9; margin: 0; padding: 20px;">
	    
	    <div class="email-container">
	      <div class="email-header">
	        <div class="logo">ClaimSureLIC</div>
	      </div>
	      <div class="email-content">
	        <div class="greeting">Congratulations, %s!</div>
	        <p>We're pleased to inform you that your application to join our network of professional insurance agents has been approved.</p>
	        
	        <div class="success-highlight">
	          <strong>Your agent account has been officially activated and you are now ready to start representing ClaimSureLIC.</strong>
	        </div>
	        
	        <p>Next steps to get started:</p>
	        <ol>
	          <li>Log in to your agent portal using your registered credentials</li>
	          <li>Complete your agent profile with additional information</li>
	          <li>Review our product catalog and commission structure</li>
	          <li>Access training materials to enhance your selling skills</li>
	        </ol>
	        
	        <a href="#" class="cta-button">Access Agent Portal</a>
	        
	        <p>As a ClaimSureLIC agent, you now have the opportunity to offer our premium insurance products to clients while earning competitive commissions. Our team is here to support your success every step of the way.</p>
	        
	        <div class="divider"></div>
	        
	        <p>We look forward to a successful partnership!</p>
	        <p>Best regards,<br>
	        <strong>ClaimSureLIC Agent Relations Team</strong></p>
	      </div>
	      <div class="email-footer">
	        © 2025 ClaimSureLIC Insurance. All rights reserved.<br>
	        <small>This is a transactional email regarding your agent status.</small>
	      </div>
	    </div>
	    </body>
	    </html>
	    """.formatted(name);
	}

	public String getAgentRejectionEmail(String name) {
	    return """
	    <!DOCTYPE html>
	    <html>
	    <head>
	      <style>
	        .email-container {
	          max-width: 600px;
	          margin: 0 auto;
	          font-family: 'Segoe UI', Arial, sans-serif;
	          color: #333333;
	          line-height: 1.6;
	        }
	        
	        .email-header {
	          background-color: #1E5C97;
	          color: white;
	          padding: 20px;
	          text-align: center;
	          border-radius: 5px 5px 0 0;
	        }
	        
	        .email-content {
	          padding: 30px;
	          background-color: #ffffff;
	          border-left: 1px solid #e5e5e5;
	          border-right: 1px solid #e5e5e5;
	        }
	        
	        .email-footer {
	          background-color: #f5f5f5;
	          padding: 20px;
	          text-align: center;
	          font-size: 14px;
	          color: #666666;
	          border-radius: 0 0 5px 5px;
	          border: 1px solid #e5e5e5;
	        }
	        
	        .greeting {
	          font-size: 22px;
	          font-weight: 600;
	          margin-bottom: 20px;
	          color: #1E5C97;
	        }
	        
	        .info-highlight {
	          background-color: #F9F9F9;
	          border-left: 4px solid #5F6368;
	          padding: 10px 15px;
	          margin: 15px 0;
	        }
	        
	        .cta-button {
	          background-color: #1E5C97;
	          color: white;
	          padding: 12px 25px;
	          text-decoration: none;
	          border-radius: 4px;
	          display: inline-block;
	          margin: 15px 0;
	          font-weight: 500;
	        }
	        
	        .divider {
	          border-top: 1px solid #eeeeee;
	          margin: 20px 0;
	        }
	        
	        .logo {
	          font-size: 24px;
	          font-weight: 700;
	          color: white;
	          text-decoration: none;
	        }
	      </style>
	    </head>
	    <body style="background-color: #f9f9f9; margin: 0; padding: 20px;">
	    
	    <div class="email-container">
	      <div class="email-header">
	        <div class="logo">ClaimSureLIC</div>
	      </div>
	      <div class="email-content">
	        <div class="greeting">Important Notice Regarding Your Application, %s</div>
	        <p>Thank you for your interest in becoming an insurance agent with ClaimSureLIC.</p>
	        
	        <div class="info-highlight">
	          <strong>After careful review of your application, we regret to inform you that we are unable to approve your agent registration at this time.</strong>
	        </div>
	        
	        <p>This decision may be due to one or more of the following factors:</p>
	        <ul>
	          <li>Incomplete or insufficient documentation</li>
	          <li>Licensing requirements not fully met</li>
	          <li>Business needs and current agent capacity in your region</li>
	          <li>Discrepancies in provided information</li>
	        </ul>
	        
	        <p>You are welcome to apply again in the future with updated information or additional qualifications. If you believe this decision was made in error or would like more specific feedback, please contact our Agent Relations department.</p>
	        
	        <a href="#" class="cta-button">Contact Agent Relations</a>
	        
	        <div class="divider"></div>
	        
	        <p>We appreciate your understanding and wish you success in your professional endeavors.</p>
	        <p>Regards,<br>
	        <strong>ClaimSureLIC Agent Relations Team</strong></p>
	      </div>
	      <div class="email-footer">
	        © 2025 ClaimSureLIC Insurance. All rights reserved.<br>
	        <small>This is a transactional email regarding your agent application.</small>
	      </div>
	    </div>
	    </body>
	    </html>
	    """.formatted(name);
	}
}
