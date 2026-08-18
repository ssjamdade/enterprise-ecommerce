package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.dto.VerifyPaymentRequest;

public interface PaymentService {
    public PaymentResponse createPayment(Long orderId);
    PaymentResponse verifyPayment(VerifyPaymentRequest request);
    void handleWebhook(String payload, String signature, String eventId);
}
