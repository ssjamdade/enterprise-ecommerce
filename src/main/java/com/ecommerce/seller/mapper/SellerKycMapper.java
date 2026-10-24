package com.ecommerce.seller.mapper;

import com.ecommerce.seller.dto.SellerKycResponse;
import com.ecommerce.seller.entity.SellerKycEntity;
import org.springframework.stereotype.Component;

@Component
public class SellerKycMapper {

    public SellerKycResponse toResponse(SellerKycEntity entity) {
        if (entity == null) {
            return null;
        }

        String userName = entity.getUser() != null
                ? (entity.getUser().getFirstName() + (entity.getUser().getLastName() != null ? " " + entity.getUser().getLastName() : ""))
                : null;

        return SellerKycResponse.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .userEmail(entity.getUser() != null ? entity.getUser().getEmail() : null)
                .userName(userName)
                .userPhone(entity.getUser() != null ? entity.getUser().getPhone() : null)
                .companyName(entity.getCompanyName())
                .storeName(entity.getStoreName())
                // Step 1
                .idType(entity.getIdType())
                .idNumber(entity.getIdNumber())
                .idDocumentUrl(entity.getIdDocumentUrl())
                .gstin(entity.getGstin())
                .panNumber(entity.getPanNumber())
                .businessDocumentUrl(entity.getBusinessDocumentUrl())
                .step1Status(entity.getStep1Status())
                .step1RejectionReason(entity.getStep1RejectionReason())
                // Step 2
                .selfieImageUrl(entity.getSelfieImageUrl())
                .step2Status(entity.getStep2Status())
                .step2RejectionReason(entity.getStep2RejectionReason())
                // Step 4
                .addressLine(entity.getAddressLine())
                .city(entity.getCity())
                .state(entity.getState())
                .pincode(entity.getPincode())
                .country(entity.getCountry())
                .addressProofDocumentUrl(entity.getAddressProofDocumentUrl())
                .step4Status(entity.getStep4Status())
                .step4RejectionReason(entity.getStep4RejectionReason())
                // Step 5
                .bankName(entity.getBankName())
                .accountHolderName(entity.getAccountHolderName())
                .accountNumber(entity.getAccountNumber())
                .ifscCode(entity.getIfscCode())
                .bankProofDocumentUrl(entity.getBankProofDocumentUrl())
                .step5Status(entity.getStep5Status())
                .step5RejectionReason(entity.getStep5RejectionReason())
                // Overall
                .overallStatus(entity.getOverallStatus())
                .adminNotes(entity.getAdminNotes())
                .verifiedAt(entity.getVerifiedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
