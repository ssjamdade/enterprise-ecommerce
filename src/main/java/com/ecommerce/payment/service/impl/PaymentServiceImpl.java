package com.ecommerce.payment.service.impl;

import com.ecommerce.auth.entity.UserEntity;
import com.ecommerce.common.exception.BadRequestException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.order.entity.OrderEntity;
import com.ecommerce.order.repo.OrderRepo;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.entity.PaymentEntity;
import com.ecommerce.payment.repository.PaymentRepo;
import com.ecommerce.payment.service.PaymentService;
import com.ecommerce.security.SecurityUtils;
import com.ecommerce.security.config.RazorpayConfig;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepo orderRepo;
    private final PaymentRepo paymentRepo;
    private final SecurityUtils securityUtils;
    private final RazorpayClient razorpayClient;

    @Override
    @Transactional
    public PaymentResponse createPayment(Long orderId) {

        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found."));

        UserEntity user = securityUtils.getCurrentUser();

        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot pay for this order.");
        }

        if (order.getPaymentStatus() == OrderEntity.PaymentStatus.PAID) {
            throw new BadRequestException("Order is already paid.");
        }

        PaymentEntity payment = paymentRepo.findByOrderId(orderId)
                .orElseGet(() -> PaymentEntity.builder()
                        .order(order)
                        .amount(order.getTotalAmount())
                        .currency("INR")
                        .status(PaymentEntity.PaymentStatus.PENDING)
                        .build());

        try {
            JSONObject options = new JSONObject();
            options.put(
                    "amount",
                    order.getTotalAmount()
                            .multiply(BigDecimal.valueOf(100))
                            .longValue()
            );
            options.put("currency", "INR");
            options.put("receipt", "order_" + order.getId());

            Order razorpayOrder = razorpayClient.orders.create(options);

            payment.setRazorpayOrderId(razorpayOrder.get("id"));
            paymentRepo.save(payment);

            return PaymentResponse.builder()
                    .paymentId(payment.getId())
                    .razorpayOrderId(payment.getRazorpayOrderId())
                    .amount(payment.getAmount())
                    .currency(payment.getCurrency())
                    .build();

        } catch (RazorpayException e) {
            throw new BadRequestException("Unable to create Razorpay order.");
        }
    }
}
