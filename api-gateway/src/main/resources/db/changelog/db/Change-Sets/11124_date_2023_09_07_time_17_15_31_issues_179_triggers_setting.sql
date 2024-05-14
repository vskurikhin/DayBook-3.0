--liquibase formatted sql
--

--
--changeset svn:11124 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11124_date_2023_09_07_time_17_15_31_issues_179_triggers_setting.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_dictionary_setting ON dictionary.setting;
CREATE TRIGGER update_create_time_dictionary_setting
    BEFORE INSERT
    ON dictionary.setting
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_dictionary_setting ON dictionary.setting;
CREATE TRIGGER update_update_time_dictionary_setting
    BEFORE UPDATE
    ON dictionary.setting
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_dictionary_setting ON dictionary.setting; DROP TRIGGER IF EXISTS update_create_time_dictionary_setting ON dictionary.setting;

