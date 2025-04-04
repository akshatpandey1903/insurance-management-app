package com.aurionpro.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


@Configuration
public class CloudinaryConfig {
	
	@Bean
	public Cloudinary cloudinary() {
		return new Cloudinary(ObjectUtils.asMap(
	            "cloud_name", "${cloudinary.cloud-name}",
	            "api_key", "${cloudinary.api-key}",
	            "api_secret", "${cloudinary.api-secret}"
	        ));
	}
}
