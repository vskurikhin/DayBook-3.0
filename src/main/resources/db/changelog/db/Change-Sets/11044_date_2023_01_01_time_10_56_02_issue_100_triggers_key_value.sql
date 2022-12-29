--liquibase formatted sql
--

--
--changeset svn:11044 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11044_date_2023_01_01_time_10_56_02_issue_100_triggers_key_value.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_dictionary_key_value ON dictionary.key_value;
CREATE TRIGGER update_create_time_dictionary_key_value
    BEFORE INSERT
    ON dictionary.key_value
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_dictionary_key_value ON dictionary.key_value;
CREATE TRIGGER update_update_time_dictionary_key_value
    BEFORE UPDATE
    ON dictionary.key_value
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_dictionary_key_value ON dictionary.key_value; DROP TRIGGER IF EXISTS update_create_time_dictionary_key_value ON dictionary.key_value;

