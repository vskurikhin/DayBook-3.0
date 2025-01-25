psql -c 'GRANT USAGE ON SCHEMA public TO replicator;' -U pguser pgdb
psql -c 'GRANT SELECT ON ALL TABLES IN SCHEMA public TO replicator;' -U pguser pgdb
psql -c 'CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA public version "1.1";' -U pguser pgdb
psql -c 'CREATE TABLE test_users (id UUID PRIMARY KEY DEFAULT uuid_generate_v4(), name TEXT, create_at timestamp DEFAULT now());' -U pguser pgdb
psql -c 'GRANT USAGE ON TABLE test_users TO replicator;' -U pguser pgdb
psql -c 'GRANT SELECT ON TABLE test_users TO replicator;' -U pguser pgdb
psql -c 'CREATE PUBLICATION pub_test_users FOR TABLE test_users;' -U pguser pgdb
psql -c 'CREATE SCHEMA IF NOT EXISTS pgdb;' -U pguser pgdb
psql -c 'CREATE SCHEMA IF NOT EXISTS dictionary;' -U pguser pgdb
psql -c 'CREATE SCHEMA IF NOT EXISTS security;' -U pguser pgdb
psql -c 'ALTER DEFAULT PRIVILEGES IN SCHEMA pgdb GRANT USAGE TO replicator;' -U pguser pgdb
psql -c 'ALTER DEFAULT PRIVILEGES IN SCHEMA pgdb GRANT SELECT ON TABLES TO replicator;' -U pguser pgdb
psql -c 'ALTER DEFAULT PRIVILEGES IN SCHEMA dictionary GRANT USAGE TO replicator;' -U pguser pgdb
psql -c 'ALTER DEFAULT PRIVILEGES IN SCHEMA dictionary GRANT SELECT ON TABLES TO replicator;' -U pguser pgdb
psql -c 'ALTER DEFAULT PRIVILEGES IN SCHEMA security GRANT USAGE TO replicator;' -U pguser pgdb
psql -c 'ALTER DEFAULT PRIVILEGES IN SCHEMA security GRANT SELECT ON TABLES TO replicator;' -U pguser pgdb