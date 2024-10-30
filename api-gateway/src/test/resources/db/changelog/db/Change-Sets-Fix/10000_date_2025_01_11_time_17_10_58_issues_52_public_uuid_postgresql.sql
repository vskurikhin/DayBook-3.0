--liquibase formatted sql
--

--
--changeset a18578179:10000 failOnError:true logicalFilePath:src/test/resources/db/changelog/db/Change-Sets-Fix/10000_date_2025_01_11_time_17_10_58_issues_52_public_uuid_postgresql.sql
--

--
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA pg_catalog version "1.1";
--

--
--rollback DROP EXTENSION IF EXISTS "uuid-ossp";