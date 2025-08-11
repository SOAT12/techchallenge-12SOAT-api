CREATE TABLE notification_employee (
    notification_id  BIGINT NOT NULL,
    employee_id      BIGINT NOT NULL,
    PRIMARY KEY (notification_id, employee_id),
    CONSTRAINT fk_notification FOREIGN KEY (notification_id) REFERENCES notification(id) ON DELETE CASCADE,
    CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE
);