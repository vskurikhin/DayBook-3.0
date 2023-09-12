--liquibase formatted sql
--

--
--changeset svn:11132 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11132_date_2023_09_10_time_14_39_18_issues_183_triggers_stanza.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_dictionary_stanza ON dictionary.stanza;
CREATE TRIGGER update_create_time_dictionary_stanza
    BEFORE INSERT
    ON dictionary.stanza
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_dictionary_stanza ON dictionary.stanza;
CREATE TRIGGER update_update_time_dictionary_stanza
    BEFORE UPDATE
    ON dictionary.stanza
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_dictionary_stanza ON dictionary.stanza; DROP TRIGGER IF EXISTS update_create_time_dictionary_stanza ON dictionary.stanza;

