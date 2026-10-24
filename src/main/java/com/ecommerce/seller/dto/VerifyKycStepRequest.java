package com.ecommerce.seller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyKycStepRequest {

    @NotBlank(message = "Step identifier is required (STEP_1, STEP_2, STEP_4, STEP_5)")
    private String step;

    @NotBlank(message = "Status is required (VERIFIED, REJECTED)")
    private String status;

    private String rejectionReason;

    private String adminNotes;
}
