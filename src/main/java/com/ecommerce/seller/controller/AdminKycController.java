package com.ecommerce.seller.controller;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.seller.dto.SellerKycResponse;
import com.ecommerce.seller.dto.VerifyKycStepRequest;
import com.ecommerce.seller.service.SellerKycService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/kyc")
@RequiredArgsConstructor
public class AdminKycController {

    private final SellerKycService sellerKycService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SellerKycResponse>>> getAllSubmissions(
            @RequestParam(required = false) String status,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<SellerKycResponse> page = sellerKycService.getAllSubmissions(status, pageable);
        return ResponseEntity.ok(ApiResponse.<Page<SellerKycResponse>>builder()
                .success(true)
                .message("KYC submissions retrieved successfully.")
                .data(page)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SellerKycResponse>> getSubmissionById(@PathVariable Long id) {
        SellerKycResponse submission = sellerKycService.getSubmissionById(id);
        return ResponseEntity.ok(ApiResponse.<SellerKycResponse>builder()
                .success(true)
                .message("KYC submission retrieved successfully.")
                .data(submission)
                .build());
    }

    @PostMapping("/{id}/verify-step")
    public ResponseEntity<ApiResponse<SellerKycResponse>> verifyStep(
            @PathVariable Long id,
            @Valid @RequestBody VerifyKycStepRequest request) {

        SellerKycResponse updated = sellerKycService.verifyStep(id, request);
        return ResponseEntity.ok(ApiResponse.<SellerKycResponse>builder()
                .success(true)
                .message("KYC step processed successfully.")
                .data(updated)
                .build());
    }
}
