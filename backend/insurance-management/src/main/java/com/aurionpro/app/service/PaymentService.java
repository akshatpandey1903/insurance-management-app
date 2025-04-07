package com.aurionpro.app.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aurionpro.app.config.RazorpayConfig;
import com.aurionpro.app.dto.CreatePaymentRequestDTO;
import com.aurionpro.app.dto.CreatePaymentResponseDTO;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class PaymentService {

    @Autowired
    private RazorpayConfig razorpayConfig;

    public CreatePaymentResponseDTO createOrder(CreatePaymentRequestDTO requestDTO) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(razorpayConfig.getKey_id(), razorpayConfig.getKey_secret());

        JSONObject options = new JSONObject();
        options.put("amount", requestDTO.getAmount().multiply(new java.math.BigDecimal(100)).intValue());
        options.put("currency", requestDTO.getCurrency());
        options.put("receipt", requestDTO.getReceipt());

        Order order = client.orders.create(options);

        CreatePaymentResponseDTO response = new CreatePaymentResponseDTO();
        response.setOrderId(order.get("id"));
        response.setCurrency(order.get("currency"));
        response.setReceipt(order.get("receipt"));
        response.setAmount(order.get("amount"));

        return response;
    }
}
