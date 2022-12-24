--liquibase formatted sql
--

--
--changeset a18578179:11009 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11009_date_2021_01_15_time_00_35_14_issues_52_dictionary_tag_label_triggers.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_dictionary_tag_label ON dictionary.tag_label;
CREATE TRIGGER update_create_time_dictionary_tag_label
    BEFORE INSERT
    ON dictionary.tag_label
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_dictionary_tag_label ON dictionary.tag_label;
CREATE TRIGGER update_update_time_dictionary_tag_label
    BEFORE UPDATE
    ON dictionary.tag_label
    FOR EACH ROW
EXECUTE FUNCTION dictionary.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_create_time_dictionary_tag_label ON dictionary.tag_label; DROP TRIGGER IF EXISTS update_update_time_dictionary_tag_label ON dictionary.tag_label;

