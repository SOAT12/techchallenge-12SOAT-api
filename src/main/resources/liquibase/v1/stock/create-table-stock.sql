CREATE TABLE stock (
    id               SERIAL PRIMARY KEY,
    created_at       TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ             DEFAULT NOW(),
    tool_name        VARCHAR(255)   NOT NULL,
    value            DECIMAL(10, 2) NOT NULL,
    active           BOOLEAN        NOT NULL DEFAULT TRUE,
    quantity         INT            NOT NULL CHECK (quantity >= 0),
    tool_category_id INT            NOT NULL,

    FOREIGN KEY (tool_category_id) REFERENCES tool_category (id)
);