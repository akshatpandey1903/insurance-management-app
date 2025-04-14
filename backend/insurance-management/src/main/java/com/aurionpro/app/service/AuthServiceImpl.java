package com.aurionpro.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.app.dto.AdminRegistrationDTO;
import com.aurionpro.app.dto.AgentRegistrationDTO;
import com.aurionpro.app.dto.CustomerRegistrationDTO;
import com.aurionpro.app.dto.EmployeeRegistrationDTO;
import com.aurionpro.app.dto.JwtAuthResponse;
import com.aurionpro.app.dto.LoginDTO;
import com.aurionpro.app.dto.UserResponseDTO;
import com.aurionpro.app.entity.Agent;
import com.aurionpro.app.entity.Customer;
import com.aurionpro.app.entity.Employee;
import com.aurionpro.app.entity.User;
import com.aurionpro.app.exceptions.UserApiException;
import com.aurionpro.app.repository.RoleRepository;
import com.aurionpro.app.repository.UserRepository;
import com.aurionpro.app.security.JwtTokenProvider;

@Service
public class AuthServiceImpl implements AuthService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private EmailService emailService;

	@Override
	public JwtAuthResponse login(LoginDTO loginDto) {
		try {
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = tokenProvider.generateToken(authentication);
			String role = authentication.getAuthorities().stream()
	                .findFirst()
	                .map(authority -> authority.getAuthority()) 
	                .map(auth -> auth.replace("ROLE_", "")) 
	                .orElse("DEFAULT");		
			JwtAuthResponse authResponse = new JwtAuthResponse();
			User user = userRepo.findByUsername(loginDto.getUsername())
					.orElseThrow(() -> new RuntimeException("Error"));
			authResponse.setUserId(user.getUserId());
			
			authResponse.setAccessToken(token);
			authResponse.setRole(role);
			authResponse.setUserId(user.getUserId());
			return authResponse;
		}catch(BadCredentialsException e) {
			throw new UserApiException(HttpStatus.NOT_FOUND, "Username or Password is incorrect");
		}
	}

	@Override
	public UserResponseDTO registerAdmin(AdminRegistrationDTO request) {
	    User admin = new User();
	    admin.setUsername(request.getUsername());
	    admin.setPassword(passwordEncoder.encode(request.getPassword()));
	    admin.setEmail(request.getEmail());
	    admin.setFirstName(request.getFirstName());
	    admin.setLastName(request.getLastName());
	    admin.setRole(roleRepo.findByRoleName("ROLE_ADMIN"));
	    admin.setActive(true);

	    userRepo.save(admin);
	    return new UserResponseDTO(admin.getUserId(), admin.getUsername(), admin.getEmail(), admin.getFirstName(), admin.getLastName(), admin.getRole().getRoleName());
	}


	@Override
	public UserResponseDTO registerCustomer(CustomerRegistrationDTO request) {
	    Customer customer = new Customer();
	    customer.setUsername(request.getUsername());
	    customer.setPassword(passwordEncoder.encode(request.getPassword()));
	    customer.setEmail(request.getEmail());
	    customer.setFirstName(request.getFirstName());
	    customer.setLastName(request.getLastName());
	    customer.setRole(roleRepo.findByRoleName("ROLE_CUSTOMER"));
	    customer.setAddress(request.getAddress());
	    customer.setPhoneNumber(request.getPhoneNumber());
	    customer.setActive(true);

	    userRepo.save(customer);
	    
	    String htmlContent = getCustomerWelcomeEmail(customer.getFirstName() + " " + customer.getLastName());
	    emailService.sendHtmlEmail(customer.getEmail(), "Welcome to ClaimSureLIC Insurance", htmlContent);
	    return new UserResponseDTO(customer.getUserId(), customer.getUsername(), customer.getEmail(), customer.getFirstName(), customer.getLastName(),customer.getRole().getRoleName());
	}


	@Override
	public UserResponseDTO registerAgent(AgentRegistrationDTO request) {
	    Agent agent = new Agent();
	    agent.setUsername(request.getUsername());
	    agent.setPassword(passwordEncoder.encode(request.getPassword()));
	    agent.setEmail(request.getEmail());
	    agent.setFirstName(request.getFirstName());
	    agent.setLastName(request.getLastName());
	    agent.setRole(roleRepo.findByRoleName("ROLE_AGENT"));
	    agent.setLicenseNumber(request.getLicenseNumber());
	    agent.setActive(true);

	    userRepo.save(agent);
	    
	    String htmlContent = getAgentWelcomeEmail(agent.getFirstName() + " " + agent.getLastName());
	    emailService.sendHtmlEmail(agent.getEmail(), "Welcome to ClaimSureLIC Insurance", htmlContent);
	    
	    return new UserResponseDTO(agent.getUserId(), agent.getUsername(), agent.getEmail(), agent.getFirstName(), agent.getLastName(),agent.getRole().getRoleName());
	}


	@Override
	public UserResponseDTO registerEmployee(EmployeeRegistrationDTO request) {
	    Employee employee = new Employee();
	    employee.setUsername(request.getUsername());
	    employee.setPassword(passwordEncoder.encode(request.getPassword()));
	    employee.setEmail(request.getEmail());
	    employee.setFirstName(request.getFirstName());
	    employee.setLastName(request.getLastName());
	    employee.setRole(roleRepo.findByRoleName("ROLE_EMPLOYEE"));
	    employee.setDepartment(request.getDepartment());
	    employee.setDesignation(request.getDesignation());
	    employee.setActive(true);

	    userRepo.save(employee);
	    
	    String htmlContent = getEmployeeWelcomeEmail(employee.getFirstName() + " " + employee.getLastName());
	    emailService.sendHtmlEmail(employee.getEmail(), "Welcome to ClaimSureLIC Insurance", htmlContent);
	    
	    return new UserResponseDTO(employee.getUserId(), employee.getUsername(), employee.getEmail(), employee.getFirstName(), employee.getLastName(), employee.getRole().getRoleName());
	}

	public String getCustomerWelcomeEmail(String name) {
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
	        <div class="greeting">Welcome to Our Insurance Platform, %s!</div>
	        <p>We're delighted to have you join us as a valued customer at ClaimSureLIC.</p>
	        
	        <div class="role-highlight">
	          <strong>Your account has been successfully registered as a Customer.</strong>
	        </div>
	        
	        <p>With your new account, you can:</p>
	        <ul>
	          <li>Browse our comprehensive range of insurance products</li>
	          <li>Access personalized insurance recommendations</li>
	          <li>Manage your policies in one secure location</li>
	          <li>File and track claims with ease</li>
	        </ul>
	        
	        <a href="http://localhost:4200/login" class="cta-button">Access Your Account</a>
	        
	        <p>If you have any questions about your account or our services, our customer support team is ready to assist you.</p>
	        
	        <div class="divider"></div>
	        
	        <p>Thank you for choosing ClaimSureLIC for your insurance needs.</p>
	        <p>Warm regards,<br>
	        <strong>The ClaimSureLIC Team</strong></p>
	      </div>
	      <div class="email-footer">
	        © 2025 ClaimSureLIC Insurance. All rights reserved.<br>
	        <small>This is a transactional email related to your account registration.</small>
	      </div>
	    </div>
	    </body>
	    </html>
	    """.formatted(name);
	}

	public String getAgentWelcomeEmail(String name) {
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
	        <div class="greeting">Welcome to the ClaimSureLIC Team, %s!</div>
	        <p>Thank you for taking the first step towards joining our network of professional insurance agents.</p>
	        
	        <div class="role-highlight">
	          <strong>Your registration as an Insurance Agent has been received and is being processed.</strong>
	        </div>
	        
	        <p>What happens next:</p>
	        <ol>
	          <li>Our employee team will review your application</li>
	          <li>You'll receive notification once your account is approved</li>
	          <li>After approval, you can access our agent portal and begin serving clients</li>
	        </ol>
	        
	        <p>While you wait for approval, you can prepare by:</p>
	        <ul>
	          <li>Familiarizing yourself with our product offerings</li>
	          <li>Reviewing our commission structure documentation</li>
	        </ul>
	        
	        <div class="divider"></div>
	        
	        <p>We look forward to partnering with you to provide exceptional insurance solutions to our customers.</p>
	        <p>Best regards,<br>
	        <strong>ClaimSureLIC Agent Relations</strong></p>
	      </div>
	      <div class="email-footer">
	        © 2025 ClaimSureLIC Insurance. All rights reserved.<br>
	        <small>This is a transactional email related to your agent registration.</small>
	      </div>
	    </div>
	    </body>
	    </html>
	    """.formatted(name);
	}

	public String getEmployeeWelcomeEmail(String name) {
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
	        <div class="greeting">Hello %s, Welcome Aboard!</div>
	        <p>We're thrilled to have you join the ClaimSureLIC team.</p>
	        
	        <div class="role-highlight">
	          <strong>You have been successfully registered as an Employee with administrative access.</strong>
	        </div>
	        
	        <p>Your role enables you to:</p>
	        <ul>
	          <li>Review and approve agent applications</li>
	          <li>Manage customer accounts and policies</li>
	          <li>Access reporting and analytics tools</li>
	          <li>Process and verify insurance claims</li>
	        </ul>
	        
	        <a href="http://localhost:4200/login" class="cta-button">Access Your Dashboard</a>
	        
	        <p>Your administrative access grants you important responsibilities in maintaining our high standards of service. If you have any questions about your role or need assistance, please reach out to your team leader.</p>
	        
	        <div class="divider"></div>
	        
	        <p>We're confident you'll make valuable contributions to our team.</p>
	        <p>Warm regards,<br>
	        <strong>ClaimSureLIC Management</strong></p>
	      </div>
	      <div class="email-footer">
	        © 2025 ClaimSureLIC Insurance. All rights reserved.<br>
	        <small>This is a transactional email related to your employee account.</small>
	      </div>
	    </div>
	    </body>
	    </html>
	    """.formatted(name);
	}

}
