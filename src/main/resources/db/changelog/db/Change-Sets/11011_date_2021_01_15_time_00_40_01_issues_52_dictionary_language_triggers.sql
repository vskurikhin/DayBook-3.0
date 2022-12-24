--liquibase formatted sql
--

--
--changeset a18578179:11011 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11011_date_2021_01_15_time_00_40_01_issues_52_dictionary_language_triggers.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_dictionary_language ON dictionary.language;
CREATE TRIGGER update_create_time_dictionary_language
    BEFORE INSERT
    ON dictionary.language
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_dictionary_language ON dictionary.language;
CREATE TRIGGER update_update_time_dictionary_language
    BEFORE UPDATE
    ON dictionary.language
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_create_time_dictionary_language ON dictionary.language; DROP TRIGGER IF EXISTS update_update_time_dictionary_language ON dictionary.language;
