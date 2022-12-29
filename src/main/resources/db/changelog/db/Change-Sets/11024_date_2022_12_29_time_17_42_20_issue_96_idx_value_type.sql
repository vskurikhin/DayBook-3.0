--liquibase formatted sql
--

--
--changeset svn:11024 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11024_date_2022_12_29_time_17_42_20_issue_96_idx_value_type.sql
--

--
CREATE INDEX IF NOT EXISTS IDX_dictionary_value_type_user_name
    ON dictionary.value_type (user_name);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_value_type_user_name;
