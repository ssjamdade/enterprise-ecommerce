package com.ecommerce.seller.entity;

import com.ecommerce.auth.entity.UserEntity;
import com.ecommerce.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seller_kyc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerKycEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "store_name", nullable = false, length = 150)
    private String storeName;

    // Step 1: Document Upload & Business Data Matching (Govt ID + Tax/Business)
    @Column(name = "id_type", nullable = false, length = 50)
    private String idType;

    @Column(name = "id_number", nullable = false, length = 100)
    private String idNumber;

    @Column(name = "id_document_url", nullable = false, columnDefinition = "TEXT")
    private String idDocumentUrl;

    @Column(name = "gstin", nullable = false, length = 20)
    private String gstin;

    @Column(name = "pan_number", nullable = false, length = 20)
    private String panNumber;

    @Column(name = "business_document_url", columnDefinition = "TEXT")
    private String businessDocumentUrl;

    @Column(name = "step1_status", nullable = false, length = 20)
    @Builder.Default
    private String step1Status = "PENDING";

    @Column(name = "step1_rejection_reason", columnDefinition = "TEXT")
    private String step1RejectionReason;

    // Step 2: Facial Recognition / Biometric Selfie Check
    @Column(name = "selfie_image_url", nullable = false, columnDefinition = "TEXT")
    private String selfieImageUrl;

    @Column(name = "step2_status", nullable = false, length = 20)
    @Builder.Default
    private String step2Status = "PENDING";

    @Column(name = "step2_rejection_reason", columnDefinition = "TEXT")
    private String step2RejectionReason;

    // Step 4: Address Verification (Physical / Address Proof Document)
    @Column(name = "address_line", nullable = false, length = 300)
    private String addressLine;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "state", nullable = false, length = 100)
    private String state;

    @Column(name = "pincode", nullable = false, length = 20)
    private String pincode;

    @Column(name = "country", nullable = false, length = 100)
    @Builder.Default
    private String country = "India";

    @Column(name = "address_proof_document_url", nullable = false, columnDefinition = "TEXT")
    private String addressProofDocumentUrl;

    @Column(name = "step4_status", nullable = false, length = 20)
    @Builder.Default
    private String step4Status = "PENDING";

    @Column(name = "step4_rejection_reason", columnDefinition = "TEXT")
    private String step4RejectionReason;

    // Step 5: Bank Account Validation (Payout Account Details)
    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;

    @Column(name = "account_holder_name", nullable = false, length = 150)
    private String accountHolderName;

    @Column(name = "account_number", nullable = false, length = 50)
    private String accountNumber;

    @Column(name = "ifsc_code", nullable = false, length = 20)
    private String ifscCode;

    @Column(name = "bank_proof_document_url", nullable = false, columnDefinition = "TEXT")
    private String bankProofDocumentUrl;

    @Column(name = "step5_status", nullable = false, length = 20)
    @Builder.Default
    private String step5Status = "PENDING";

    @Column(name = "step5_rejection_reason", columnDefinition = "TEXT")
    private String step5RejectionReason;

    // Overall Status: PENDING_REVIEW, ACTION_REQUIRED, APPROVED, REJECTED
    @Column(name = "overall_status", nullable = false, length = 30)
    @Builder.Default
    private String overallStatus = "PENDING_REVIEW";

    @Column(name = "admin_notes", columnDefinition = "TEXT")
    private String adminNotes;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;
}
