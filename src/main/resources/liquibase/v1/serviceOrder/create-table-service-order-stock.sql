CREATE TABLE service_order_stock (
    service_order_id  BIGINT NOT NULL,
    stock_id          BIGINT NOT NULL,
    PRIMARY KEY (service_order_id, stock_id),
    CONSTRAINT fk_soss_service_order
        FOREIGN KEY (service_order_id) REFERENCES service_order (id) ON DELETE CASCADE,
    CONSTRAINT fk_soss_stock
        FOREIGN KEY (stock_id) REFERENCES stock (id)
);