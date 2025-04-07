package com.aurionpro.app.dto;

import lombok.Data;

@Data
public class CustomerReportDto {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private long totalPolicies;
    
    public CustomerReportDto(int userId, String firstName, String lastName,
    	String email, String phoneNumber, long totalPolicies) {
    	this.userId = userId;
    	this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.totalPolicies = totalPolicies;
    }
}
