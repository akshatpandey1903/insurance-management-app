package com.aurionpro.app.config;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class RazorpayConfig {

    private RazorpayClient razorpayClient;
    
    @Value("${razorpay.key-id}")
    private String key_id;
    
    @Value("${razorpay.key-secret}")
    private String key_secret;

    public RazorpayConfig() throws RazorpayException {
        this.razorpayClient = new RazorpayClient(key_id, key_secret);
    }

    public RazorpayClient getClient() {
        return razorpayClient;
    }
}
