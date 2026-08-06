CREATE TABLE inventory (
    id BIGSERIAL PRIMARY KEY,

    product_id BIGINT NOT NULL UNIQUE,

    quantity INTEGER NOT NULL DEFAULT 0,

    reserved_quantity INTEGER NOT NULL DEFAULT 0,

    low_stock_threshold INTEGER NOT NULL DEFAULT 5,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_inventory_product
        FOREIGN KEY(product_id)
        REFERENCES products(id)
        ON DELETE CASCADE
);