package com.aurionpro.app.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.aurionpro.app.error.UserErrorResponse;

@ControllerAdvice
public class GlobalException 
{
	@ExceptionHandler(UserApiException.class)
	public ResponseEntity<UserErrorResponse> handleAccountApiException(UserApiException e)
	{
		
		UserErrorResponse response = new UserErrorResponse();
		
		response.setMessage(e.getMessage());
		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setTime(System.currentTimeMillis());
		
		return new ResponseEntity<UserErrorResponse>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleAccountApiException(MethodArgumentNotValidException e)
	{
		
		Map<String, String> errors = new HashMap<>();
		
		e.getBindingResult().getFieldErrors().forEach((error)->{
			errors.put(error.getField(),error.getDefaultMessage());
		});
		
	
		
		return new ResponseEntity<>(errors,HttpStatus.NOT_FOUND);
	}
	
}
