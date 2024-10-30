--liquibase formatted sql
--

--
--changeset a18578179:10002 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/10002_date_2021_01_14_time_17_20_39_issues_52_schemas.sql
--

--
CREATE SCHEMA IF NOT EXISTS db;
CREATE SCHEMA IF NOT EXISTS dictionary;
CREATE SCHEMA IF NOT EXISTS security;
--

--
--rollback DROP SCHEMA IF EXISTS security; DROP SCHEMA IF EXISTS dictionary; DROP SCHEMA IF EXISTS db;