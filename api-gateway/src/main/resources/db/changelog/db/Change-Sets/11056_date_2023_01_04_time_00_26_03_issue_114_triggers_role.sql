--liquibase formatted sql
--

--
--changeset svn:11056 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11056_date_2023_01_04_time_00_26_03_issue_114_triggers_role.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_security_role ON security.role;
CREATE TRIGGER update_create_time_security_role
    BEFORE INSERT
    ON security.role
    FOR EACH ROW
EXECUTE FUNCTION security.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_security_role ON security.role;
CREATE TRIGGER update_update_time_security_role
    BEFORE UPDATE
    ON security.role
    FOR EACH ROW
EXECUTE FUNCTION security.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_security_role ON security.role; DROP TRIGGER IF EXISTS update_create_time_security_role ON security.role;

