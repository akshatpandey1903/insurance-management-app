package com.aurionpro.app.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class UserErrorResponse 
{
		private String message;
		private int status;
		private long time;
}

