/*
 * This file was last modified at 2022.03.24 13:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 135_date_2021_01_14_time_17_30_08_issues_52_security_user_name_id_index.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:135 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/135_date_2021_01_14_time_17_30_08_issues_52_security_user_name_id_index.sql
--

--
CREATE INDEX IF NOT EXISTS IDX_security_user_name_id
    ON security.user_name (id);
--

--
--rollback DROP INDEX IF EXISTS security.IDX_security_user_name_id;
