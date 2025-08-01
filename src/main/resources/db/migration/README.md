# Flyway Database Migrations

This directory contains Flyway migration scripts for the Junie Project database schema.

## Migration Scripts

- `V1__create_beer_table.sql` - Creates the initial beer table structure

## Naming Convention

Flyway migration scripts follow the naming convention:

```
V{version}__{description}.sql
```

Where:
- `V` indicates a versioned migration
- `{version}` is a version number (can include dots and underscores)
- `__` (double underscore) separates the version from the description
- `{description}` is a brief description of the migration using underscores for spaces
- `.sql` is the file extension

Examples:
- `V1__create_tables.sql`
- `V1.1__add_indexes.sql`
- `V2023.07.31__add_audit_columns.sql`

## Testing

Migrations are tested in the `FlywayMigrationTest` class, which verifies:

1. The migrations run successfully
2. The expected tables are created
3. The tables have the expected structure

## Configuration

Flyway is configured in `application.yml` with the following settings:

```yaml
spring:
  flyway:
    enabled: true
    locations: classpath:db/migration
    table: flyway_schema_history
    baseline-on-migrate: false
    validate-on-migrate: true
```