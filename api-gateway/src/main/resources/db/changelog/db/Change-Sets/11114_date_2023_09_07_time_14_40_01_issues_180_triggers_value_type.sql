--liquibase formatted sql
--

--
--changeset svn:11114 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11114_date_2023_09_07_time_14_40_01_issues_180_triggers_value_type.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_dictionary_value_type ON dictionary.value_type;
CREATE TRIGGER update_create_time_dictionary_value_type
    BEFORE INSERT
    ON dictionary.value_type
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_dictionary_value_type ON dictionary.value_type;
CREATE TRIGGER update_update_time_dictionary_value_type
    BEFORE UPDATE
    ON dictionary.value_type
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_dictionary_value_type ON dictionary.value_type; DROP TRIGGER IF EXISTS update_create_time_dictionary_value_type ON dictionary.value_type;

