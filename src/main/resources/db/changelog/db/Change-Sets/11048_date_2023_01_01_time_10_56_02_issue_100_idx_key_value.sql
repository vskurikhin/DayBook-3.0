--liquibase formatted sql
--

--
--changeset svn:11048 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11048_date_2023_01_01_time_10_56_02_issue_100_idx_key_value.sql
--

--

CREATE INDEX IF NOT EXISTS IDX_dictionary_key_value_user_name
    ON dictionary.key_value (user_name);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_key_value_user_name;

