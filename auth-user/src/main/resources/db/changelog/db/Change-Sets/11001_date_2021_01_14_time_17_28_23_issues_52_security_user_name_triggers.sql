--liquibase formatted sql
--

--
--changeset a18578179:11001 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11001_date_2021_01_14_time_17_28_23_issues_52_security_user_name_triggers.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_security_user_name ON security.user_name;
CREATE TRIGGER update_create_time_security_user_name
    BEFORE INSERT
    ON security.user_name
    FOR EACH ROW
EXECUTE FUNCTION security.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_security_user_name ON security.user_name;
CREATE TRIGGER update_update_time_security_user_name
    BEFORE UPDATE
    ON security.user_name
    FOR EACH ROW
EXECUTE FUNCTION security.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_security_user_name ON security.user_name; DROP TRIGGER IF EXISTS update_create_time_security_user_name ON security.user_name;
