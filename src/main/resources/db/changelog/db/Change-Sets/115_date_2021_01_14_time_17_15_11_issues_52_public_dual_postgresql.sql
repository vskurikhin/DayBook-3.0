/*
 * This file was last modified at 2022.03.23 22:47 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 115_date_2021_01_14_time_17_15_11_issues_52_public_dual_postgresql.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:115 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/115_date_2021_01_14_time_17_15_11_issues_52_public_dual_postgresql.sql
--

--
CREATE TABLE IF NOT EXISTS dual AS ( VALUES (true) );
--

--
--rollback DROP TABLE IF EXISTS dual;