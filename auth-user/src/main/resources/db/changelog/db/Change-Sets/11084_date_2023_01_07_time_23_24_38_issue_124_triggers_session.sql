--liquibase formatted sql
--

--
--changeset svn:11084 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11084_date_2023_01_07_time_23_24_38_issue_124_triggers_session.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_security_session ON security.session;
CREATE TRIGGER update_create_time_security_session
    BEFORE INSERT
    ON security.session
    FOR EACH ROW
EXECUTE FUNCTION security.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_security_session ON security.session;
CREATE TRIGGER update_update_time_security_session
    BEFORE UPDATE
    ON security.session
    FOR EACH ROW
EXECUTE FUNCTION security.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_security_session ON security.session; DROP TRIGGER IF EXISTS update_create_time_security_session ON security.session;

