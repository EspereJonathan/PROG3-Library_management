CREATE TABLE IF NOT EXISTS "author" (
    "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    "name" VARCHAR(255) NOT NULL,
    "sex" CHAR(1) CHECK (sex IN ('M', 'F')) NOT NULL
);