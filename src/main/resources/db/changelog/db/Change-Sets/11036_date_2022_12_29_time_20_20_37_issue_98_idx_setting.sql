--liquibase formatted sql
--

--
--changeset svn:11036 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11036_date_2022_12_29_time_20_20_37_issue_98_idx_setting.sql
--

--

CREATE INDEX IF NOT EXISTS IDX_dictionary_setting_user_name
    ON dictionary.setting (user_name);

CREATE INDEX IF NOT EXISTS IDX_dictionary_setting_value_type_id
    ON dictionary.setting (value_type_id);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_setting_value_type_id; DROP INDEX IF EXISTS dictionary.IDX_dictionary_setting_user_name;

