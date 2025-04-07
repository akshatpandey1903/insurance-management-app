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
import com.aurionpro.app.security.CustomUserDetailsService;
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
			
			authResponse.setAccessToken(token);
			authResponse.setRole(role);
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
	    return new UserResponseDTO(employee.getUserId(), employee.getUsername(), employee.getEmail(), employee.getFirstName(), employee.getLastName(), employee.getRole().getRoleName());
	}

	
}
