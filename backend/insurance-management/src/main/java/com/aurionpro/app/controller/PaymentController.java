package com.aurionpro.app.controller;

import com.aurionpro.app.dto.CreatePremiumPaymentRequestDTO;
import com.aurionpro.app.dto.PaymentOrderResponseDTO;
import com.aurionpro.app.dto.VerifyPaymentRequestDTO;
import com.aurionpro.app.service.CustomerPolicyService;
import com.aurionpro.app.service.PaymentService;
import com.aurionpro.app.service.TransactionService;
import com.razorpay.RazorpayException;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private CustomerPolicyService customerPolicyService;

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/create-order")
	public PaymentOrderResponseDTO createOrder(@RequestBody CreatePremiumPaymentRequestDTO request) throws RazorpayException {
		String orderId = paymentService.createPaymentOrder(request.getCustomerPolicyId(), request.getAmount());
		int amountInPaise = request.getAmount().multiply(BigDecimal.valueOf(100)).intValue();
		return new PaymentOrderResponseDTO(orderId, "INR", amountInPaise);
	}

	@PostMapping("/verify")
	public ResponseEntity<String> verifyPayment(@RequestBody VerifyPaymentRequestDTO request) throws RazorpayException {
		boolean isValid = paymentService.verifyPayment(
			request.getRazorpayOrderId(),
			request.getRazorpayPaymentId(),
			request.getRazorpaySignature()
		);

		if (isValid) {
			transactionService.recordPremiumTransaction(request.getCustomerPolicyId(), request.getRazorpayPaymentId());

			return ResponseEntity.ok("Payment verified and recorded");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verification failed");
	}
	
	@PostMapping("/premium/order/{policyId}")
    public ResponseEntity<?> createOrder(@PathVariable int policyId, @RequestParam int customerId) {
        String orderId = transactionService.createPremiumPaymentOrder(policyId, customerId);
        return ResponseEntity.ok(Map.of("orderId", orderId));
    }

    @PostMapping("/premium/verify")
    public ResponseEntity<?> verifyAndRecord(@RequestBody VerifyPaymentRequestDTO dto, @RequestParam int customerId) {
        transactionService.verifyAndRecordPremiumTransaction(dto, customerId);
        return ResponseEntity.ok("Payment verified and premium recorded");
    }
}

