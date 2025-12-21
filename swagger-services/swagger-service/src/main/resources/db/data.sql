-- Initialize roles data
-- Using simple INSERT statements with H2-compatible syntax
-- For BIGINT milliseconds: H2 supports DATEDIFF with MILLISECOND unit
-- Using CURRENT_TIMESTAMP for TIMESTAMP fields
INSERT INTO roles (name, status, created_on, created_at, created_by, updated_on, updated_at, updated_by)
SELECT 'ADMIN', 'ACTIVE', 
       DATEDIFF('MS', TIMESTAMP '1970-01-01 00:00:00', CURRENT_TIMESTAMP), 
       CURRENT_TIMESTAMP, 'system', 
       DATEDIFF('MS', TIMESTAMP '1970-01-01 00:00:00', CURRENT_TIMESTAMP), 
       CURRENT_TIMESTAMP, 'system'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO roles (name, status, created_on, created_at, created_by, updated_on, updated_at, updated_by)
SELECT 'USER', 'ACTIVE', 
       DATEDIFF('MS', TIMESTAMP '1970-01-01 00:00:00', CURRENT_TIMESTAMP), 
       CURRENT_TIMESTAMP, 'system', 
       DATEDIFF('MS', TIMESTAMP '1970-01-01 00:00:00', CURRENT_TIMESTAMP), 
       CURRENT_TIMESTAMP, 'system'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');

INSERT INTO roles (name, status, created_on, created_at, created_by, updated_on, updated_at, updated_by)
SELECT 'CREATOR', 'ACTIVE', 
       DATEDIFF('MS', TIMESTAMP '1970-01-01 00:00:00', CURRENT_TIMESTAMP), 
       CURRENT_TIMESTAMP, 'system', 
       DATEDIFF('MS', TIMESTAMP '1970-01-01 00:00:00', CURRENT_TIMESTAMP), 
       CURRENT_TIMESTAMP, 'system'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'CREATOR');

INSERT INTO roles (name, status, created_on, created_at, created_by, updated_on, updated_at, updated_by)
SELECT 'COLLABORATOR', 'ACTIVE', 
       DATEDIFF('MS', TIMESTAMP '1970-01-01 00:00:00', CURRENT_TIMESTAMP), 
       CURRENT_TIMESTAMP, 'system', 
       DATEDIFF('MS', TIMESTAMP '1970-01-01 00:00:00', CURRENT_TIMESTAMP), 
       CURRENT_TIMESTAMP, 'system'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'COLLABORATOR');

INSERT INTO roles (name, status, created_on, created_at, created_by, updated_on, updated_at, updated_by)
SELECT 'EDITOR', 'ACTIVE', 
       DATEDIFF('MS', TIMESTAMP '1970-01-01 00:00:00', CURRENT_TIMESTAMP), 
       CURRENT_TIMESTAMP, 'system', 
       DATEDIFF('MS', TIMESTAMP '1970-01-01 00:00:00', CURRENT_TIMESTAMP), 
       CURRENT_TIMESTAMP, 'system'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'EDITOR');

