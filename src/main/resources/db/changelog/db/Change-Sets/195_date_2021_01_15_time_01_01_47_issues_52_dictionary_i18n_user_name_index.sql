/*
 * This file was last modified at 2022.03.24 13:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 195_date_2021_01_15_time_01_01_47_issues_52_dictionary_i18n_user_name_index.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:195 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/195_date_2021_01_15_time_01_01_47_issues_52_dictionary_i18n_user_name_index.sql
--

--
CREATE INDEX IF NOT EXISTS IDX_dictionary_i18n_user_name
    ON dictionary.i18n (user_name);
--

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_i18n_user_name;
