package com.aurionpro.app.service;

import com.aurionpro.app.dto.AdminProfileDto;
import com.aurionpro.app.dto.CustomerProfileDTO;

public interface CustomerProfileService {
	CustomerProfileDTO getProfile(String username);
    void updateProfile(String username, CustomerProfileDTO dto);
    AdminProfileDto getAdminProfile(String username);
    void updateAdminProfile(String username , AdminProfileDto dto);
}
