package com.aurionpro.app.service;

public interface PasswordResetTokenService 
{
	public void createPasswordResetToken(String email);
	public void resetPassword(String token,String newPassword);
}
