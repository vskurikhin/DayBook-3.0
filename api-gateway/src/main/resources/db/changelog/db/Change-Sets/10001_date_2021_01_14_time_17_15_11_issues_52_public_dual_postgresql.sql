--liquibase formatted sql
--

--
--changeset a18578179:10001 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/10001_date_2021_01_14_time_17_15_11_issues_52_public_dual_postgresql.sql
--

--
CREATE TABLE IF NOT EXISTS dual AS ( VALUES (true) );
--

--
--rollback DROP TABLE IF EXISTS dual;