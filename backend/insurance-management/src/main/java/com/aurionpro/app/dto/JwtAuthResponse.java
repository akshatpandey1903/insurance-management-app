package com.aurionpro.app.dto;


import com.aurionpro.app.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class JwtAuthResponse {
	private String accessToken;
	private String tokenType="Bearer";
	private String role; // line chng
	private int userId;
}