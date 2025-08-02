CREATE TABLE tool_category (
    id                 SERIAL PRIMARY KEY,
    tool_category_name VARCHAR(255) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE
);
