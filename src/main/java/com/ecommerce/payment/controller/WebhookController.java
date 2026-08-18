package com.ecommerce.payment.controller;

import com.ecommerce.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final PaymentService paymentService;

    @PostMapping(
            value = "/razorpay",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> webhook(
            @RequestBody String payload,
            @RequestHeader("X-Razorpay-Signature") String signature,
            @RequestHeader(value = "x-razorpay-event-id", required = false) String eventId) {

        paymentService.handleWebhook(payload, signature, eventId);

        return ResponseEntity.ok().build();
    }
}
