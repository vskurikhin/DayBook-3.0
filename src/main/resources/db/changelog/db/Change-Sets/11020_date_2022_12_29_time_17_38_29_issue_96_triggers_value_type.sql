--liquibase formatted sql
--

--
--changeset svn:11020 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11020_date_2022_12_29_time_17_38_29_issue_96_triggers_value_type.sql
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

