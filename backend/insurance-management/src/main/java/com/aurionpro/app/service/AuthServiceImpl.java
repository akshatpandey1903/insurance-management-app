package com.aurionpro.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.aurionpro.app.dto.JwtAuthResponse;
import com.aurionpro.app.dto.LoginDTO;
import com.aurionpro.app.dto.UserRequestDTO;
import com.aurionpro.app.dto.UserResponseDTO;
import com.aurionpro.app.entity.Role;
import com.aurionpro.app.entity.User;
import com.aurionpro.app.exceptions.UserApiException;
import com.aurionpro.app.repository.RoleRepository;
import com.aurionpro.app.repository.UserRepository;
import com.aurionpro.app.security.JwtTokenProvider;

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
			JwtAuthResponse authResponse = new JwtAuthResponse();
			authResponse.setAccessToken(token);
			return authResponse;
		}catch(BadCredentialsException e) {
			throw new UserApiException(HttpStatus.NOT_FOUND, "Username or Password is incorrect");
		}
	}

	@Override
	public UserResponseDTO register(UserRequestDTO requestDto) {
		if(userRepo.existsByUserName(requestDto.getUsername())) {
			throw new UserApiException(HttpStatus.BAD_REQUEST, "Username already exists");
		}
		
		Role role = roleRepo.findByRoleName(requestDto.getRoleName()).get();
		User user = new User();
		user.setActive(true);
		user.setUsername(requestDto.getUsername());
		user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		user.setEmail(requestDto.getEmail());
		user.setFirstName(requestDto.getFirstName());
		user.setLastName(requestDto.getLastName());
		user.setRole(role);
		
		User savedUser = userRepo.save(user);
		
		return new UserResponseDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getFirstName(), savedUser.getLastName(), savedUser.getRole().getRoleName());
	}
	
}
