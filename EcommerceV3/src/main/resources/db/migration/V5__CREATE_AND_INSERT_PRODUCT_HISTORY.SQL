
CREATE TABLE _product_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    active bit not null,
    product_id BIGINT NOT NULL,
    product_history_id BIGINT,
    version INT NOT NULL,
    product_name VARCHAR(255),
    product_description TEXT,
    product_price double not null,
    product_category VARCHAR(50),
    currency_id BIGINT,
    stock INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creation_date DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    deleted_date DATETIME(6) NULL,
    updated_date DATETIME(6) NULL,

    FOREIGN KEY (product_id) REFERENCES _product(id),
    FOREIGN KEY (currency_id) REFERENCES _currency(id)

);

ALTER TABLE _item
    ADD CONSTRAINT fk_item_product_history FOREIGN KEY (product_history_id) REFERENCES _product_history(id);


INSERT INTO _product_history
(id, active, creation_date, deleted_date, updated_date, product_id, version, product_name, product_description, product_price, product_category, currency_id, stock)
VALUES
    (1, TRUE, '2025-06-02 14:41:20', NULL, NULL, 2, 1, 'Kamen Rider W cinto duplo', 'top dmais', 150, 'CATEGORY2', 1, 4),
    (2, TRUE, '2025-06-02 14:40:21', NULL, NULL, 1, 1, 'Kamen Rider Ji Fox GEATS Desire Drive', 'Descrição do Produto 5', 400, 'CATEGORY2', 1, 5),
    (3, TRUE, '2025-06-02 14:44:14', NULL, NULL, 5, 1, 'kamen Rider action figure Geats mk9', 'top dmais tambem', 250, 'CATEGORY1', 1, 4),
    (4, TRUE, '2025-06-02 14:44:49', NULL, NULL, 6, 1, 'kamen Rider action figure W', 'top dmais tambem', 120, 'CATEGORY1', 1, 4),
    (5, TRUE, '2025-06-02 14:47:07', NULL, NULL, 7, 1, 'kamen Rider action figure Decade', 'top dmais tambem', 155, 'CATEGORY1', 1, 3),
    (6, TRUE, '2025-06-02 14:42:10', NULL, NULL, 3, 1, 'Kamen Rider Zero-One Belt Dx', 'top dmais tambem', 100, 'CATEGORY2', 1, 7);
