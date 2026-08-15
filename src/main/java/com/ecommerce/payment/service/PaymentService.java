package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.PaymentResponse;

public interface PaymentService {
    public PaymentResponse createPayment(Long orderId);
}
