/*
 * This file was last modified at 2022.03.23 22:47 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 120_date_2021_01_14_time_17_20_39_issues_52_schemas.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:120 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/120_date_2021_01_14_time_17_20_39_issues_52_schemas.sql
--

--
CREATE SCHEMA IF NOT EXISTS db;
CREATE SCHEMA IF NOT EXISTS dictionary;
CREATE SCHEMA IF NOT EXISTS security;
--

--
--rollback DROP SCHEMA IF EXISTS security; DROP SCHEMA IF EXISTS dictionary; DROP SCHEMA IF EXISTS db;