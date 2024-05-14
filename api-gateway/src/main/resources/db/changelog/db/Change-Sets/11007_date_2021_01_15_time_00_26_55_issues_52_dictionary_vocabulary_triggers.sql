--liquibase formatted sql
--

--
--changeset a18578179:11007 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11007_date_2021_01_15_time_00_26_55_issues_52_dictionary_vocabulary_triggers.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_dictionary_vocabulary ON dictionary.vocabulary;
CREATE TRIGGER update_create_time_dictionary_vocabulary
    BEFORE INSERT
    ON dictionary.vocabulary
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_dictionary_vocabulary ON dictionary.vocabulary;
CREATE TRIGGER update_update_time_dictionary_vocabulary
    BEFORE UPDATE
    ON dictionary.vocabulary
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_dictionary_vocabulary ON dictionary.vocabulary; DROP TRIGGER IF EXISTS update_create_time_dictionary_vocabulary ON dictionary.vocabulary;
