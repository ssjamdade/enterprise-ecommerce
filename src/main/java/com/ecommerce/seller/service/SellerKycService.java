package com.ecommerce.seller.service;

import com.ecommerce.seller.dto.SellerKycResponse;
import com.ecommerce.seller.dto.SellerKycSubmissionRequest;
import com.ecommerce.seller.dto.VerifyKycStepRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SellerKycService {

    SellerKycResponse getMyKyc();

    SellerKycResponse submitKyc(SellerKycSubmissionRequest request);

    Page<SellerKycResponse> getAllSubmissions(String status, Pageable pageable);

    SellerKycResponse getSubmissionById(Long id);

    SellerKycResponse verifyStep(Long kycId, VerifyKycStepRequest request);

    boolean isCurrentUserApprovedSeller();
}
