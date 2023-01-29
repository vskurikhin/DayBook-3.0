--liquibase formatted sql
--

--
--changeset 18578179:11106 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11106_date_2023_01_25_time_09_26_35_issues_160_dictionary_i18n_idx_i18n.sql
--

--

CREATE INDEX IF NOT EXISTS IDX_dictionary_i18n_user_name
    ON dictionary.i18n (user_name);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_i18n_user_name;

