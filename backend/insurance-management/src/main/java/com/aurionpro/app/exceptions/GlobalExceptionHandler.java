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
public class GlobalExceptionHandler 
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
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<UserErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e)
	{
		
		UserErrorResponse response = new UserErrorResponse();
		
		response.setMessage(e.getMessage());
		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setTime(System.currentTimeMillis());
		
		return new ResponseEntity<UserErrorResponse>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EmailExistsException.class)
	public ResponseEntity<UserErrorResponse> handleResourceNotFoundException(EmailExistsException e)
	{
		
		UserErrorResponse response = new UserErrorResponse();
		
		response.setMessage("This Email is already registered with our application");
		response.setStatus(HttpStatus.CONFLICT.value());
		response.setTime(System.currentTimeMillis());
		response.setField("email");
		
		return new ResponseEntity<UserErrorResponse>(response,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UsernameExistsException.class)
	public ResponseEntity<UserErrorResponse> handleUsernameExistsException(UsernameExistsException e) 
	{
	    
		UserErrorResponse response = new UserErrorResponse();
		
	    response.setMessage("Username is already taken.");
	    response.setStatus(HttpStatus.CONFLICT.value());
	    response.setTime(System.currentTimeMillis());
	    response.setField("username");
	    
	    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
}
