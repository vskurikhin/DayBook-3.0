--liquibase formatted sql
--

--
--changeset a18578179:11013 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11013_date_2021_01_15_time_00_59_20_issues_52_dictionary_i18n_triggers.sql
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
--rollback DROP TRIGGER IF EXISTS update_create_time_dictionary_i18n ON dictionary.i18n; DROP TRIGGER IF EXISTS update_update_time_dictionary_i18n ON dictionary.i18n;
