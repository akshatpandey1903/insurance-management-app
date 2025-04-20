package com.aurionpro.app.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UsernameExistsException extends RuntimeException 
{
	public UsernameExistsException(String message) {
        super(message);
    }
}
