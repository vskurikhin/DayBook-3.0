--liquibase formatted sql
--

--
--changeset 18578179:11094 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11094_date_2023_01_24_time_12_11_53_issues_158_dictionary_language_idx_language.sql
--

--

CREATE INDEX IF NOT EXISTS IDX_dictionary_language_user_name
    ON dictionary.language (user_name);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_language_user_name;

