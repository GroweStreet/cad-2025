CREATE TABLE IF NOT EXISTS CATEGORIES (
    category_id INT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS PRODUCTS (
    product_id     INT PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    description    VARCHAR(255),
    category_id    INT,
    price          DECIMAL(10, 2),
    stock_quantity INT,
    image_url      VARCHAR(255),
    created_at     TIMESTAMP,
    updated_at     TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES CATEGORIES (category_id)
);
