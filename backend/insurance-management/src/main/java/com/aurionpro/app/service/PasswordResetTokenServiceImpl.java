package com.aurionpro.app.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aurionpro.app.entity.PasswordResetToken;
import com.aurionpro.app.entity.User;
import com.aurionpro.app.exceptions.UserApiException;
import com.aurionpro.app.repository.PasswordResetTokenRepository;
import com.aurionpro.app.repository.UserRepository;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService
{
	
	 @Autowired
	 private PasswordResetTokenRepository tokenRepository;
	    
	 @Autowired
	 private UserRepository userRepository;
	 
	 @Autowired
	 private EmailService emailService;
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	
	@Override
	public void createPasswordResetToken(String email) 
	{
		User user = userRepository.findByEmail(email).orElseThrow(()-> new UserApiException(HttpStatus.NOT_FOUND, "User not found"));
		
		String token = UUID.randomUUID().toString();
		
		PasswordResetToken resetToken = new PasswordResetToken();
		
		resetToken.setToken(token);
		resetToken.setUser(user);
		resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // 30 minutes validity
		
		tokenRepository.save(resetToken);
		
		String resetLink = "http://localhost:4200/forgot-password?token=" + token;
		emailService.sendEmail(user.getEmail(),"Reset Password","Click the link to reset: " + resetLink);
		
	}

	@Override
	public void resetPassword(String token, String newPassword) 
	{
		PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new UserApiException(HttpStatus.BAD_REQUEST, "Invalid token"));
		
		if(resetToken.getExpiryDate().isBefore(LocalDateTime.now()))
		{
			throw new UserApiException(HttpStatus.BAD_REQUEST, "Token expired");
		}
		
		User user = resetToken.getUser();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		
		tokenRepository.delete(resetToken);
	}

}
