-- Step 1, 2, 4, 5 Amazon-style Seller KYC Verification Table
CREATE TABLE seller_kyc (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    company_name VARCHAR(150) NOT NULL,
    store_name VARCHAR(150) NOT NULL,

    -- Step 1: Document Upload & Business Data Matching (Govt ID + Tax/Business)
    id_type VARCHAR(50) NOT NULL,
    id_number VARCHAR(100) NOT NULL,
    id_document_url TEXT NOT NULL,
    gstin VARCHAR(20) NOT NULL,
    pan_number VARCHAR(20) NOT NULL,
    business_document_url TEXT,
    step1_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    step1_rejection_reason TEXT,

    -- Step 2: Facial Recognition / Biometric Selfie Check
    selfie_image_url TEXT NOT NULL,
    step2_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    step2_rejection_reason TEXT,

    -- Step 4: Address Verification (Physical / Address Proof Document)
    address_line VARCHAR(300) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL DEFAULT 'India',
    address_proof_document_url TEXT NOT NULL,
    step4_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    step4_rejection_reason TEXT,

    -- Step 5: Bank Account Validation (Payout Account Details)
    bank_name VARCHAR(100) NOT NULL,
    account_holder_name VARCHAR(150) NOT NULL,
    account_number VARCHAR(50) NOT NULL,
    ifsc_code VARCHAR(20) NOT NULL,
    bank_proof_document_url TEXT NOT NULL,
    step5_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    step5_rejection_reason TEXT,

    -- Overall Status: PENDING_REVIEW, ACTION_REQUIRED, APPROVED, REJECTED
    overall_status VARCHAR(30) NOT NULL DEFAULT 'PENDING_REVIEW',
    admin_notes TEXT,
    verified_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_seller_kyc_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- Link products to sellers
ALTER TABLE products 
ADD COLUMN seller_id BIGINT REFERENCES users(id) ON DELETE SET NULL;
