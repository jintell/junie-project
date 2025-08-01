-- V4__create_beer_order_line_table.sql
-- Creates the beer_order_line table structure

CREATE TABLE IF NOT EXISTS beer_order_line (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    beer_order_id INT NOT NULL,
    beer_id INT NOT NULL,
    order_quantity INT NOT NULL,
    created_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (beer_order_id) REFERENCES beer_order(id),
    FOREIGN KEY (beer_id) REFERENCES beer(id)
);

-- Add a comment explaining the purpose of this table
COMMENT ON TABLE beer_order_line IS 'Stores individual beer order line items';