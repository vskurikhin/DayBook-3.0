/*
 * This file was last modified at 2022.03.24 13:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 192_date_2021_01_15_time_01_00_04_issues_52_dictionary_i18n_language_id_index.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:190 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/192_date_2021_01_15_time_01_00_04_issues_52_dictionary_i18n_language_id_index.sql
--

--
CREATE INDEX IF NOT EXISTS IDX_dictionary_i18n_language_id
    ON dictionary.i18n (language_id);
--

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_i18n_language_id;
