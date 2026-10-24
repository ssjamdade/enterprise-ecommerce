package com.ecommerce.seller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerKycSubmissionRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Store / Display name is required")
    private String storeName;

    // Step 1: Identity & Business Details
    @NotBlank(message = "Government ID type is required")
    private String idType;

    @NotBlank(message = "Government ID number is required")
    private String idNumber;

    @NotBlank(message = "Government ID document URL is required")
    private String idDocumentUrl;

    @NotBlank(message = "GSTIN is required")
    private String gstin;

    @NotBlank(message = "PAN number is required")
    private String panNumber;

    private String businessDocumentUrl;

    // Step 2: Facial Recognition / Biometric Selfie Check
    @NotBlank(message = "Selfie image URL is required")
    private String selfieImageUrl;

    // Step 4: Address Verification
    @NotBlank(message = "Address line is required")
    private String addressLine;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    private String pincode;

    @Builder.Default
    private String country = "India";

    @NotBlank(message = "Address proof document URL is required")
    private String addressProofDocumentUrl;

    // Step 5: Bank Account Validation
    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "Account holder name is required")
    private String accountHolderName;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "IFSC code is required")
    private String ifscCode;

    @NotBlank(message = "Bank proof document URL is required")
    private String bankProofDocumentUrl;
}
