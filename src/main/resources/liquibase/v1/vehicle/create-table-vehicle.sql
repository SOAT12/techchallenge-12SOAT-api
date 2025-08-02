CREATE TABLE vehicle (
    id            SERIAL PRIMARY KEY,
    created_at    TIMESTAMPTZ        NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ                 DEFAULT NOW(),
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    brand         VARCHAR(50)        NOT NULL,
    model         VARCHAR(50)        NOT NULL,
    year          INT CHECK (year > 1900) ,
    color VARCHAR(30),
	active BOOLEAN NOT NULL DEFAULT TRUE
);