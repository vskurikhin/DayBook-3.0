--liquibase formatted sql
--

--
--changeset a18578179:11003 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11003_date_2021_01_15_time_00_10_10_issue_52_dictionary_codifier_triggers.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_dictionary_codifier ON dictionary.codifier;
CREATE TRIGGER update_create_time_dictionary_codifier
    BEFORE INSERT
    ON dictionary.codifier
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_dictionary_codifier ON dictionary.codifier;
CREATE TRIGGER update_update_time_dictionary_codifier
    BEFORE UPDATE
    ON dictionary.codifier
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_dictionary_codifier ON dictionary.codifier; DROP TRIGGER IF EXISTS update_create_time_dictionary_codifier ON dictionary.codifier;

