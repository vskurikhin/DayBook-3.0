--liquibase formatted sql
--

--
--changeset svn:11128 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11128_date_2023_09_07_time_17_15_31_issues_179_idx_setting.sql
--

--

CREATE INDEX IF NOT EXISTS IDX_dictionary_setting_user_name
    ON dictionary.setting (user_name);
CREATE INDEX IF NOT EXISTS IDX_dictionary_setting_value_type_id
    ON dictionary.setting (value_type_id);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_setting_value_type_id;
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_setting_user_name;
