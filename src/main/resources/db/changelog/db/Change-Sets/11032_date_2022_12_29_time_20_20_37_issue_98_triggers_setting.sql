--liquibase formatted sql
--

--
--changeset svn:11032 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11032_date_2022_12_29_time_20_20_37_issue_98_triggers_setting.sql
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

