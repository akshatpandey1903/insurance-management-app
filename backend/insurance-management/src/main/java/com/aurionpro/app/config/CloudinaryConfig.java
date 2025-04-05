package com.aurionpro.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
	
	@Value("${cloudinary.cloud-name")
	private String cloudName;
	
	@Value("${cloudinary.api-key}")
	private String cloudApiKey;
	
	@Value("${cloudinary.api-secret}")
	private String cloudApiSecret;
	
	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
	            "cloud_name", "akcloud1903",
	            "api_key", cloudApiKey,
	            "api_secret", cloudApiSecret
	        ));
	}
}
