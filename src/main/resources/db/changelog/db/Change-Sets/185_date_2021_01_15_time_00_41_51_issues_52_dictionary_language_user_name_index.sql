--liquibase formatted sql
--

-- This file was last modified at 2022.03.23 22:47 by Victor N. Skurikhin.
-- This is free and unencumbered software released into the public domain.
-- For more information, please refer to <http://unlicense.org>
-- 185_date_2021_01_15_time_00_41_51_issues_52_dictionary_language_user_name_index.sql

--
--changeset a18578179:185 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/185_date_2021_01_15_time_00_41_51_issues_52_dictionary_language_user_name_index.sql
--

--
CREATE INDEX IF NOT EXISTS IDX_dictionary_language_user_name
    ON dictionary.language (user_name);
--

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_language_user_name;
