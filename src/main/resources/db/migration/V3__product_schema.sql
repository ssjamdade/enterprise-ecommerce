CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    sku VARCHAR(100) NOT NULL UNIQUE,
    price NUMERIC(12,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    category_id BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
);

CREATE TABLE product_images (
    id BIGSERIAL PRIMARY KEY,

    product_id BIGINT NOT NULL,

    image_url TEXT NOT NULL,

    is_primary BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_product_image
        FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE CASCADE
);