package com.aurionpro.app.config;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "razorpay")
@Data
public class RazorpayConfig {
	@Value("${razorpay.key-id}")
    private String key;
	@Value("${razorpay.key-secret}")
    private String secret;
}
