ALTER TABLE employee ADD COLUMN temporary_password VARCHAR;

ALTER TABLE employee ADD COLUMN password_validity TIMESTAMPTZ;

ALTER TABLE employee ADD COLUMN use_temporary_password BOOLEAN DEFAULT FALSE NOT NULL;