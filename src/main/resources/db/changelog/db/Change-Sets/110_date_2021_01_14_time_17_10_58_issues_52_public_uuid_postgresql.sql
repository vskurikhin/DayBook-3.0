--liquibase formatted sql
--

-- This file was last modified at 2022.03.23 22:47 by Victor N. Skurikhin.
-- This is free and unencumbered software released into the public domain.
-- For more information, please refer to <http://unlicense.org>
-- 110_date_2021_01_14_time_17_10_58_issues_52_public_uuid_postgresql.sql

--
--changeset a18578179:110 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/110_date_2021_01_14_time_17_10_58_issues_52_public_uuid_postgresql.sql
--

--
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" SCHEMA pg_catalog version "1.1";
--

--
--rollback DROP EXTENSION IF EXISTS "uuid-ossp";