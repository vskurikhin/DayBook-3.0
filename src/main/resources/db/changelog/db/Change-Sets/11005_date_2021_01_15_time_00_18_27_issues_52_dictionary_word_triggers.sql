--liquibase formatted sql
--

--
--changeset a18578179:11005 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11005_date_2021_01_15_time_00_18_27_issues_52_dictionary_word_triggers.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_dictionary_word ON dictionary.word;
CREATE TRIGGER update_create_time_dictionary_word
    BEFORE INSERT
    ON dictionary.word
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_dictionary_word ON dictionary.word;
CREATE TRIGGER update_update_time_dictionary_word
    BEFORE UPDATE
    ON dictionary.word
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_dictionary_word ON dictionary.word; DROP TRIGGER IF EXISTS update_create_time_dictionary_word ON dictionary.word;

