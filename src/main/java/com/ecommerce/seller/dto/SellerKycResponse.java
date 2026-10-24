package com.ecommerce.seller.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerKycResponse {

    private Long id;
    private Long userId;
    private String userEmail;
    private String userName;
    private String userPhone;

    private String companyName;
    private String storeName;

    // Step 1: Identity & Business Details
    private String idType;
    private String idNumber;
    private String idDocumentUrl;
    private String gstin;
    private String panNumber;
    private String businessDocumentUrl;
    private String step1Status;
    private String step1RejectionReason;

    // Step 2: Facial Recognition / Biometric Selfie Check
    private String selfieImageUrl;
    private String step2Status;
    private String step2RejectionReason;

    // Step 4: Address Verification
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private String addressProofDocumentUrl;
    private String step4Status;
    private String step4RejectionReason;

    // Step 5: Bank Account Validation
    private String bankName;
    private String accountHolderName;
    private String accountNumber;
    private String ifscCode;
    private String bankProofDocumentUrl;
    private String step5Status;
    private String step5RejectionReason;

    // Overall Status: PENDING_REVIEW, ACTION_REQUIRED, APPROVED, REJECTED
    private String overallStatus;
    private String adminNotes;
    private LocalDateTime verifiedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
