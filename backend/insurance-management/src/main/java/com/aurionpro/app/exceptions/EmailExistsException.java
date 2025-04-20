package com.aurionpro.app.exceptions;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EmailExistsException extends RuntimeException {

	public EmailExistsException(String message) {
        super(message);
    }

}
