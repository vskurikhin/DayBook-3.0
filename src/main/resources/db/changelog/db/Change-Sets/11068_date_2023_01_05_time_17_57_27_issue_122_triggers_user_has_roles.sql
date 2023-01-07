--liquibase formatted sql
--

--
--changeset svn:11068 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11068_date_2023_01_05_time_17_57_27_issue_122_triggers_user_has_roles.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_security_user_has_roles ON security.user_has_roles;
CREATE TRIGGER update_create_time_security_user_has_roles
    BEFORE INSERT
    ON security.user_has_roles
    FOR EACH ROW
EXECUTE FUNCTION security.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_security_user_has_roles ON security.user_has_roles;
CREATE TRIGGER update_update_time_security_user_has_roles
    BEFORE UPDATE
    ON security.user_has_roles
    FOR EACH ROW
EXECUTE FUNCTION security.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_security_user_has_roles ON security.user_has_roles; DROP TRIGGER IF EXISTS update_create_time_security_user_has_roles ON security.user_has_roles;

