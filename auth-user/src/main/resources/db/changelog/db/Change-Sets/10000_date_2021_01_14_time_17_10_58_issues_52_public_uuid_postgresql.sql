--liquibase formatted sql
--

--
--changeset a18578179:10000 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/10000_date_2021_01_14_time_17_10_58_issues_52_public_uuid_postgresql
--

--
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA pg_catalog version "1.1";
--

--
--rollback DROP EXTENSION IF EXISTS "uuid-ossp";