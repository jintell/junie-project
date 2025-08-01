-- V3__create_beer_order_table.sql
-- Creates the beer_order table structure

CREATE TABLE IF NOT EXISTS beer_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    order_status VARCHAR(50) NOT NULL,
    customer_id INT NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

-- Add a comment explaining the purpose of this table
COMMENT ON TABLE beer_order IS 'Stores beer order information';