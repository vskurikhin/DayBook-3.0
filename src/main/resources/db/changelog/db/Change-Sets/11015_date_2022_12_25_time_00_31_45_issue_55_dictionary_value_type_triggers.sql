--liquibase formatted sql
--

--
--changeset svn:11015 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11015_date_2022_12_25_time_00_31_45_issue_55_dictionary_value_type_triggers.sql
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
