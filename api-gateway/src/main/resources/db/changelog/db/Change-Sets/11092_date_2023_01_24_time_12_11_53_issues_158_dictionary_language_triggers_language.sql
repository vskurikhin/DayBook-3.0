--liquibase formatted sql
--

--
--changeset 18578179:11092 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11092_date_2023_01_24_time_12_11_53_issues_158_dictionary_language_triggers_language.sql
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
--rollback DROP TRIGGER IF EXISTS update_update_time_dictionary_language ON dictionary.language; DROP TRIGGER IF EXISTS update_create_time_dictionary_language ON dictionary.language;

