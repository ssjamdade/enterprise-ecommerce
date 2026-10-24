package com.ecommerce.seller.service.impl;

import com.ecommerce.auth.entity.RoleEntity;
import com.ecommerce.auth.entity.UserEntity;
import com.ecommerce.auth.repository.RoleRepo;
import com.ecommerce.auth.repository.UserRepo;
import com.ecommerce.common.exception.BadRequestException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.security.SecurityUtils;
import com.ecommerce.seller.dto.SellerKycResponse;
import com.ecommerce.seller.dto.SellerKycSubmissionRequest;
import com.ecommerce.seller.dto.VerifyKycStepRequest;
import com.ecommerce.seller.entity.SellerKycEntity;
import com.ecommerce.seller.mapper.SellerKycMapper;
import com.ecommerce.seller.repository.SellerKycRepo;
import com.ecommerce.seller.service.SellerKycService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerKycServiceImpl implements SellerKycService {

    private final SellerKycRepo sellerKycRepo;
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final SecurityUtils securityUtils;
    private final SellerKycMapper sellerKycMapper;

    @Override
    @Transactional(readOnly = true)
    public SellerKycResponse getMyKyc() {
        UserEntity currentUser = securityUtils.getCurrentUser();
        return sellerKycRepo.findByUserId(currentUser.getId())
                .map(sellerKycMapper::toResponse)
                .orElse(null);
    }

    @Override
    @Transactional
    public SellerKycResponse submitKyc(SellerKycSubmissionRequest request) {
        UserEntity currentUser = securityUtils.getCurrentUser();

        SellerKycEntity kyc = sellerKycRepo.findByUserId(currentUser.getId())
                .orElse(null);

        if (kyc == null) {
            kyc = SellerKycEntity.builder()
                    .user(currentUser)
                    .companyName(request.getCompanyName())
                    .storeName(request.getStoreName())
                    // Step 1
                    .idType(request.getIdType())
                    .idNumber(request.getIdNumber())
                    .idDocumentUrl(request.getIdDocumentUrl())
                    .gstin(request.getGstin())
                    .panNumber(request.getPanNumber())
                    .businessDocumentUrl(request.getBusinessDocumentUrl())
                    .step1Status("PENDING")
                    // Step 2
                    .selfieImageUrl(request.getSelfieImageUrl())
                    .step2Status("PENDING")
                    // Step 4
                    .addressLine(request.getAddressLine())
                    .city(request.getCity())
                    .state(request.getState())
                    .pincode(request.getPincode())
                    .country(request.getCountry() != null ? request.getCountry() : "India")
                    .addressProofDocumentUrl(request.getAddressProofDocumentUrl())
                    .step4Status("PENDING")
                    // Step 5
                    .bankName(request.getBankName())
                    .accountHolderName(request.getAccountHolderName())
                    .accountNumber(request.getAccountNumber())
                    .ifscCode(request.getIfscCode())
                    .bankProofDocumentUrl(request.getBankProofDocumentUrl())
                    .step5Status("PENDING")
                    // Overall
                    .overallStatus("PENDING_REVIEW")
                    .build();
        } else {
            // Update company/store names
            kyc.setCompanyName(request.getCompanyName());
            kyc.setStoreName(request.getStoreName());

            // If Step 1 was not verified or was rejected, allow resubmission
            if (!"VERIFIED".equalsIgnoreCase(kyc.getStep1Status())) {
                kyc.setIdType(request.getIdType());
                kyc.setIdNumber(request.getIdNumber());
                kyc.setIdDocumentUrl(request.getIdDocumentUrl());
                kyc.setGstin(request.getGstin());
                kyc.setPanNumber(request.getPanNumber());
                kyc.setBusinessDocumentUrl(request.getBusinessDocumentUrl());
                kyc.setStep1Status("PENDING");
                kyc.setStep1RejectionReason(null);
            }

            // If Step 2 was not verified, allow resubmission
            if (!"VERIFIED".equalsIgnoreCase(kyc.getStep2Status())) {
                kyc.setSelfieImageUrl(request.getSelfieImageUrl());
                kyc.setStep2Status("PENDING");
                kyc.setStep2RejectionReason(null);
            }

            // If Step 4 was not verified, allow resubmission
            if (!"VERIFIED".equalsIgnoreCase(kyc.getStep4Status())) {
                kyc.setAddressLine(request.getAddressLine());
                kyc.setCity(request.getCity());
                kyc.setState(request.getState());
                kyc.setPincode(request.getPincode());
                if (request.getCountry() != null) {
                    kyc.setCountry(request.getCountry());
                }
                kyc.setAddressProofDocumentUrl(request.getAddressProofDocumentUrl());
                kyc.setStep4Status("PENDING");
                kyc.setStep4RejectionReason(null);
            }

            // If Step 5 was not verified, allow resubmission
            if (!"VERIFIED".equalsIgnoreCase(kyc.getStep5Status())) {
                kyc.setBankName(request.getBankName());
                kyc.setAccountHolderName(request.getAccountHolderName());
                kyc.setAccountNumber(request.getAccountNumber());
                kyc.setIfscCode(request.getIfscCode());
                kyc.setBankProofDocumentUrl(request.getBankProofDocumentUrl());
                kyc.setStep5Status("PENDING");
                kyc.setStep5RejectionReason(null);
            }

            // If all 4 are verified already, stay APPROVED; otherwise set PENDING_REVIEW
            if ("VERIFIED".equalsIgnoreCase(kyc.getStep1Status()) &&
                "VERIFIED".equalsIgnoreCase(kyc.getStep2Status()) &&
                "VERIFIED".equalsIgnoreCase(kyc.getStep4Status()) &&
                "VERIFIED".equalsIgnoreCase(kyc.getStep5Status())) {
                kyc.setOverallStatus("APPROVED");
            } else {
                kyc.setOverallStatus("PENDING_REVIEW");
            }
        }

        SellerKycEntity saved = sellerKycRepo.save(kyc);
        log.info("KYC submitted for user id {}: overallStatus={}", currentUser.getId(), saved.getOverallStatus());
        return sellerKycMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SellerKycResponse> getAllSubmissions(String status, Pageable pageable) {
        if (status != null && !status.isBlank() && !"ALL".equalsIgnoreCase(status)) {
            return sellerKycRepo.findAllByOverallStatus(status.toUpperCase(), pageable)
                    .map(sellerKycMapper::toResponse);
        }
        return sellerKycRepo.findAll(pageable)
                .map(sellerKycMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SellerKycResponse getSubmissionById(Long id) {
        return sellerKycRepo.findById(id)
                .map(sellerKycMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("KYC submission not found with id: " + id));
    }

    @Override
    @Transactional
    public SellerKycResponse verifyStep(Long kycId, VerifyKycStepRequest request) {
        SellerKycEntity kyc = sellerKycRepo.findById(kycId)
                .orElseThrow(() -> new ResourceNotFoundException("KYC submission not found with id: " + kycId));

        String step = request.getStep() != null ? request.getStep().trim().toUpperCase() : "";
        String status = request.getStatus() != null ? request.getStatus().trim().toUpperCase() : "";

        if (!status.equals("VERIFIED") && !status.equals("REJECTED")) {
            throw new BadRequestException("Status must be either VERIFIED or REJECTED");
        }

        if (status.equals("REJECTED") && (request.getRejectionReason() == null || request.getRejectionReason().isBlank())) {
            throw new BadRequestException("Rejection reason is required when rejecting a verification step");
        }

        switch (step) {
            case "STEP_1" -> {
                kyc.setStep1Status(status);
                kyc.setStep1RejectionReason(status.equals("REJECTED") ? request.getRejectionReason() : null);
            }
            case "STEP_2" -> {
                kyc.setStep2Status(status);
                kyc.setStep2RejectionReason(status.equals("REJECTED") ? request.getRejectionReason() : null);
            }
            case "STEP_4" -> {
                kyc.setStep4Status(status);
                kyc.setStep4RejectionReason(status.equals("REJECTED") ? request.getRejectionReason() : null);
            }
            case "STEP_5" -> {
                kyc.setStep5Status(status);
                kyc.setStep5RejectionReason(status.equals("REJECTED") ? request.getRejectionReason() : null);
            }
            default -> throw new BadRequestException("Invalid step identifier. Must be STEP_1, STEP_2, STEP_4, or STEP_5");
        }

        if (request.getAdminNotes() != null && !request.getAdminNotes().isBlank()) {
            kyc.setAdminNotes(request.getAdminNotes());
        }

        // Recompute overall status
        boolean anyRejected = "REJECTED".equalsIgnoreCase(kyc.getStep1Status()) ||
                              "REJECTED".equalsIgnoreCase(kyc.getStep2Status()) ||
                              "REJECTED".equalsIgnoreCase(kyc.getStep4Status()) ||
                              "REJECTED".equalsIgnoreCase(kyc.getStep5Status());

        boolean allVerified = "VERIFIED".equalsIgnoreCase(kyc.getStep1Status()) &&
                              "VERIFIED".equalsIgnoreCase(kyc.getStep2Status()) &&
                              "VERIFIED".equalsIgnoreCase(kyc.getStep4Status()) &&
                              "VERIFIED".equalsIgnoreCase(kyc.getStep5Status());

        if (anyRejected) {
            kyc.setOverallStatus("ACTION_REQUIRED");
            log.info("KYC id {} updated: step {} rejected. Overall status set to ACTION_REQUIRED", kycId, step);
        } else if (allVerified) {
            kyc.setOverallStatus("APPROVED");
            kyc.setVerifiedAt(LocalDateTime.now());
            log.info("KYC id {} all steps VERIFIED! Promoting user {} to ROLE_SELLER", kycId, kyc.getUser().getEmail());

            // Grant ROLE_SELLER to user
            UserEntity user = kyc.getUser();
            RoleEntity sellerRole = roleRepo.findByName(RoleEntity.Role.ROLE_SELLER)
                    .orElseThrow(() -> new ResourceNotFoundException("ROLE_SELLER does not exist"));

            boolean hasRole = user.getRoles().stream()
                    .anyMatch(r -> r.getName() == RoleEntity.Role.ROLE_SELLER);

            if (!hasRole) {
                user.getRoles().add(sellerRole);
                userRepo.save(user);
            }
        } else {
            kyc.setOverallStatus("PENDING_REVIEW");
        }

        SellerKycEntity saved = sellerKycRepo.save(kyc);
        return sellerKycMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCurrentUserApprovedSeller() {
        try {
            UserEntity currentUser = securityUtils.getCurrentUser();
            if (currentUser == null) return false;

            boolean hasRole = currentUser.getRoles().stream()
                    .anyMatch(r -> r.getName() == RoleEntity.Role.ROLE_SELLER || r.getName() == RoleEntity.Role.ROLE_ADMIN);

            if (hasRole) return true;

            return sellerKycRepo.findByUserId(currentUser.getId())
                    .map(kyc -> "APPROVED".equalsIgnoreCase(kyc.getOverallStatus()))
                    .orElse(false);
        } catch (Exception e) {
            return false;
        }
    }
}
