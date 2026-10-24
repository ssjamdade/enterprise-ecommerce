package com.ecommerce.seller.controller;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.product.service.ImageService;
import com.ecommerce.seller.dto.SellerKycResponse;
import com.ecommerce.seller.dto.SellerKycSubmissionRequest;
import com.ecommerce.seller.service.SellerKycService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/seller/kyc")
@RequiredArgsConstructor
public class SellerKycController {

    private final SellerKycService sellerKycService;
    private final ImageService imageService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<SellerKycResponse>> getMyKyc() {
        SellerKycResponse kyc = sellerKycService.getMyKyc();
        return ResponseEntity.ok(ApiResponse.<SellerKycResponse>builder()
                .success(true)
                .message("Seller KYC retrieved successfully.")
                .data(kyc)
                .build());
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<SellerKycResponse>> submitKyc(
            @Valid @RequestBody SellerKycSubmissionRequest request) {

        SellerKycResponse kyc = sellerKycService.submitKyc(request);
        return ResponseEntity.ok(ApiResponse.<SellerKycResponse>builder()
                .success(true)
                .message("Seller KYC submitted for review successfully.")
                .data(kyc)
                .build());
    }

    @PostMapping(value = "/upload-document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        String fileUrl = imageService.upload(file);
        return ResponseEntity.ok(ApiResponse.<Map<String, String>>builder()
                .success(true)
                .message("Document uploaded successfully.")
                .data(Map.of("url", fileUrl))
                .build());
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkSellerStatus() {
        boolean isApproved = sellerKycService.isCurrentUserApprovedSeller();
        return ResponseEntity.ok(ApiResponse.<Map<String, Boolean>>builder()
                .success(true)
                .message("Seller status checked.")
                .data(Map.of("approved", isApproved))
                .build());
    }
}
