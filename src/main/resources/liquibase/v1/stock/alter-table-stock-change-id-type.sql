CREATE EXTENSION IF NOT EXISTS "pgcrypto";

ALTER TABLE stock DROP COLUMN tool_category_id;
ALTER TABLE stock ADD COLUMN tool_category_id UUID;

ALTER TABLE stock ADD CONSTRAINT fk_stock_tool_category
  FOREIGN KEY (tool_category_id) REFERENCES tool_category (id);

ALTER TABLE service_order_stock DROP CONSTRAINT fk_soss_stock;

ALTER TABLE stock DROP CONSTRAINT stock_pkey;
ALTER TABLE stock DROP COLUMN id;

ALTER TABLE stock ADD COLUMN id UUID PRIMARY KEY DEFAULT gen_random_uuid();

ALTER TABLE service_order_stock DROP COLUMN stock_id;
ALTER TABLE service_order_stock ADD COLUMN stock_id UUID;

ALTER TABLE service_order_stock ADD CONSTRAINT fk_soss_stock
  FOREIGN KEY (stock_id) REFERENCES stock (id);