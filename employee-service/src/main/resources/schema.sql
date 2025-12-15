-- H2 Database Schema for Employee Service
CREATE TABLE IF NOT EXISTS employees (
    id VARCHAR(32) NOT NULL PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

