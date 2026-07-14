-- V1: initial schema for the example Task aggregate.
-- Generic, template-only migration - replace with your own domain tables.

CREATE TABLE tasks (
    id          UUID         PRIMARY KEY,
    title       VARCHAR(200) NOT NULL,
    description VARCHAR(2000) NOT NULL DEFAULT '',
    status      VARCHAR(20)  NOT NULL DEFAULT 'TODO',
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX idx_tasks_status ON tasks (status);
