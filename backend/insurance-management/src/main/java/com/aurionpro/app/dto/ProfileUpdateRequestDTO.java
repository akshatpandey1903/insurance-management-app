package com.aurionpro.app.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequestDTO {
    private int employeeId;
    private String username;
    private String newPassword;
}
