/*
 * This file was last modified at 2022.03.23 22:47 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 161_date_2021_01_15_time_00_28_02_issues_52_dictionary_vocabulary_user_name_index.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:161 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/161_date_2021_01_15_time_00_28_02_issues_52_dictionary_vocabulary_user_name_index.sql
--

--
CREATE INDEX IF NOT EXISTS IDX_dictionary_vocabulary_user_name
    ON dictionary.vocabulary (user_name);
--

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_vocabulary_user_name;
