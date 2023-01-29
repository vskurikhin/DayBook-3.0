--liquibase formatted sql
--

--
--changeset 18578179:11102 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11102_date_2023_01_25_time_09_26_35_issues_160_dictionary_i18n_triggers_i18n.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_dictionary_i18n ON dictionary.i18n;
CREATE TRIGGER update_create_time_dictionary_i18n
    BEFORE INSERT
    ON dictionary.i18n
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_dictionary_i18n ON dictionary.i18n;
CREATE TRIGGER update_update_time_dictionary_i18n
    BEFORE UPDATE
    ON dictionary.i18n
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_dictionary_i18n ON dictionary.i18n; DROP TRIGGER IF EXISTS update_create_time_dictionary_i18n ON dictionary.i18n;

