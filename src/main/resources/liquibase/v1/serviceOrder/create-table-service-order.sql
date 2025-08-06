CREATE TABLE service_order
(
    id          SERIAL PRIMARY KEY,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ             DEFAULT NOW(),
    finished_at TIMESTAMPTZ,
    customer_id INT            NOT NULL,
    vehicle_id  INT            NOT NULL,
    employee_id INT            NOT NULL,
    status      VARCHAR(30)    NOT NULL,
    total_value DECIMAL(10, 2) NOT NULL,
    notes       TEXT,

    FOREIGN KEY (customer_id) REFERENCES customer (id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle (id),
    FOREIGN KEY (employee_id) REFERENCES employee (id)
);