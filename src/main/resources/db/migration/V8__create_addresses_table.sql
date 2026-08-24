CREATE TABLE addresses (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL,

    address_line VARCHAR(500) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL DEFAULT 'India',

    address_type VARCHAR(20) NOT NULL DEFAULT 'HOME',
    is_default BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_address_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_addresses_user_id
ON addresses(user_id);