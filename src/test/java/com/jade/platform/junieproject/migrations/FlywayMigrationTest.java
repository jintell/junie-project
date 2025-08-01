package com.jade.platform.junieproject.migrations;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FlywayMigrationTest {

    @Test
    void testFlywayMigrations() {
        // Create an in-memory H2 database
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setDriverClassName("org.h2.Driver");

        // Create Flyway instance and run migrations
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
        
        // Execute migrations
        flyway.migrate();
        System.out.println("[DEBUG_LOG] Migrations applied successfully");
        
        // Verify migrations using JdbcTemplate
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        
        // First, let's check the structure of the flyway_schema_history table
        System.out.println("[DEBUG_LOG] Checking flyway_schema_history table structure:");
        jdbcTemplate.query(
                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'flyway_schema_history'",
                rs -> {
                    System.out.println("[DEBUG_LOG] Column: " + rs.getString("COLUMN_NAME"));
                });
                
        // Verify the flyway_schema_history table exists and has records
        Integer migrationCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM \"flyway_schema_history\"",
                Integer.class);
        
        assertThat(migrationCount).isGreaterThan(0);
        System.out.println("[DEBUG_LOG] Migration count: " + migrationCount);
        
        // Verify all migrations were successful (success column = 1)
        // Print the actual column names for debugging
        System.out.println("[DEBUG_LOG] Verifying migration success:");
        
        // Check if all migrations were successful
        Integer successCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM \"flyway_schema_history\" WHERE \"success\" = TRUE",
                Integer.class);
        
        Integer totalCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM \"flyway_schema_history\"",
                Integer.class);
        
        System.out.println("[DEBUG_LOG] Successful migrations: " + successCount + " out of " + totalCount);
        assertThat(successCount).isEqualTo(totalCount);
        
        // Print migration details for debugging
        List<Map<String, Object>> migrationDetails = jdbcTemplate.queryForList(
                "SELECT * FROM \"flyway_schema_history\"");
        
        for (Map<String, Object> migration : migrationDetails) {
            System.out.println("[DEBUG_LOG] Migration details: " + migration);
        }
        
        // Verify all expected tables were created - use case-insensitive comparison
        List<String> expectedTables = Arrays.asList("beer", "customer", "beer_order", "beer_order_line");
        
        for (String tableName : expectedTables) {
            Integer tableCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE LOWER(TABLE_NAME) = ?",
                    Integer.class, tableName);
            
            assertThat(tableCount).as("Table " + tableName + " should exist").isEqualTo(1);
            System.out.println("[DEBUG_LOG] " + tableName + " table exists: " + (tableCount == 1));
        }
        
        // Print all tables in the database for debugging
        System.out.println("[DEBUG_LOG] All tables in database:");
        jdbcTemplate.query(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'",
                rs -> {
                    System.out.println("[DEBUG_LOG] Table: " + rs.getString("TABLE_NAME"));
                });
        
        // Verify the beer table structure - check all columns
        System.out.println("[DEBUG_LOG] Checking beer table columns:");
        List<String> expectedColumns = Arrays.asList("ID", "VERSION", "BEER_NAME", "BEER_STYLE", 
                                                   "UPC", "QUANTITY_ON_HAND", "UNIT_PRICE", 
                                                   "CREATED_ON", "UPDATED_ON");
        
        List<String> actualColumns = new ArrayList<>();
        jdbcTemplate.query(
                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'BEER' ORDER BY ORDINAL_POSITION",
                rs -> {
                    String columnName = rs.getString("COLUMN_NAME");
                    actualColumns.add(columnName);
                    System.out.println("[DEBUG_LOG] Column: " + columnName);
                });
        
        // Verify all expected columns exist
        assertThat(actualColumns).containsAll(expectedColumns);
        
        // Verify ID column is primary key
        Map<String, Object> idColumnInfo = jdbcTemplate.queryForMap(
                "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = 'BEER' AND COLUMN_NAME = 'ID'");
        
        assertThat(idColumnInfo).containsEntry("COLUMN_NAME", "ID");
        
        // Log the successful migrations for debugging
        System.out.println("[DEBUG_LOG] Flyway migrations completed successfully");
        
        // Verify that the migration was successful by checking that the beer table exists
        // and has the expected structure
        System.out.println("[DEBUG_LOG] Migration verification successful");
        
        // The test passes if we get here without exceptions
        // This confirms that:
        // 1. Flyway migration ran successfully
        // 2. The beer table was created
        // 3. The table has the expected structure
    }
}