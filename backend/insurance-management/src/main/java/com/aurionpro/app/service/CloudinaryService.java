package com.aurionpro.app.service;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
	String uploadFile(MultipartFile file);
}
