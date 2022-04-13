-- liquibase formatted sql

-- changeset dimaskop18@gmail.com:database-rescue_hr dbms:postgresql
-- precondition
DROP SCHEMA IF EXISTS rh CASCADE;

CREATE SCHEMA rh;

CREATE TABLE IF NOT EXISTS rh.ranks
(
    rank_id SERIAL PRIMARY KEY,
    rank_title VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS rh.positions
(
    position_id SERIAL PRIMARY KEY,
    position_title VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS rh.subdivisions
(
    subdivision_id SERIAL PRIMARY KEY,
    subdivision_title VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS rh.positions_subdivisions_link
(
    positions_subdivisions_link_id SERIAL PRIMARY KEY,
    subdivision_id INTEGER NOT NULL REFERENCES rh.subdivisions,
    position_id INTEGER NOT NULL REFERENCES rh.positions,
    state_amount INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS rh.employees
(
    employee_id SERIAL PRIMARY KEY,
    surname VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    patronymic VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    rank_id INTEGER NOT NULL REFERENCES rh.ranks,
    position_id INTEGER NOT NULL REFERENCES rh.positions,
    subdivision_id INTEGER NOT NULL REFERENCES rh.subdivisions
);

CREATE TABLE IF NOT EXISTS rh.positions_log
(
    positions_log_id SERIAL PRIMARY KEY,
    position_getting_date DATE NOT NULL,
    employee_id INTEGER NOT NULL REFERENCES rh.employees,
    position_id INTEGER NOT NULL REFERENCES rh.positions,
    subdivision_id INTEGER NOT NULL REFERENCES rh.subdivisions,
    order_publisher VARCHAR(255) NOT NULL,
    order_number INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS rh.ranks_log
(
    ranks_log_id SERIAL PRIMARY KEY,
    rank_getting_date DATE NOT NULL,
    employee_id INTEGER NOT NULL REFERENCES rh.employees,
    rank_id INTEGER NOT NULL REFERENCES rh.ranks,
    order_publisher VARCHAR(255) NOT NULL,
    order_number INTEGER NOT NULL
);