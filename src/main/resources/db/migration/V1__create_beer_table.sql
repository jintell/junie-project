-- V1__create_beer_table.sql
-- Creates the initial beer table structure

CREATE TABLE IF NOT EXISTS beer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    beer_name VARCHAR(255) NOT NULL,
    beer_style VARCHAR(255) NOT NULL,
    upc VARCHAR(255) NOT NULL UNIQUE,
    quantity_on_hand INT,
    unit_price DECIMAL(19, 2) NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add a comment explaining the purpose of this table
COMMENT ON TABLE beer IS 'Stores beer product information';