CREATE EXTENSION IF NOT EXISTS "pgcrypto";

ALTER TABLE stock DROP CONSTRAINT stock_tool_category_id_fkey;

ALTER TABLE tool_category DROP CONSTRAINT tool_category_pkey;
ALTER TABLE tool_category DROP COLUMN id;

ALTER TABLE tool_category ADD COLUMN id UUID PRIMARY KEY DEFAULT gen_random_uuid();

