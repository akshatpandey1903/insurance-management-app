package com.aurionpro.app.dto;

import lombok.Data;

@Data
public class CustomerProfileDTO {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String currentPassword;
}
