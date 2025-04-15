package com.aurionpro.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerBasicDTO {
    private int id;
    private String fullName;
    private String email;
}

