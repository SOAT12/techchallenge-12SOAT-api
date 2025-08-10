CREATE TABLE service_order_vehicle_service (
    service_order_id   BIGINT         NOT NULL,
    vehicle_service_id BIGINT         NOT NULL,
    PRIMARY KEY (service_order_id, vehicle_service_id),
    CONSTRAINT fk_sos_service_order
        FOREIGN KEY (service_order_id) REFERENCES service_order (id) ON DELETE CASCADE,
    CONSTRAINT fk_sos_service
        FOREIGN KEY (vehicle_service_id) REFERENCES vehicle_service (id)
);