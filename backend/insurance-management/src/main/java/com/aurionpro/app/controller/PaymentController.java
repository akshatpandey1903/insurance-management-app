package com.aurionpro.app.controller;

import com.aurionpro.app.dto.CreatePaymentRequestDTO;
import com.aurionpro.app.dto.CreatePaymentResponseDTO;
import com.aurionpro.app.service.PaymentService;
import com.razorpay.RazorpayException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public CreatePaymentResponseDTO createOrder(@RequestBody CreatePaymentRequestDTO requestDTO) throws RazorpayException {
        return paymentService.createOrder(requestDTO);
    }
}
