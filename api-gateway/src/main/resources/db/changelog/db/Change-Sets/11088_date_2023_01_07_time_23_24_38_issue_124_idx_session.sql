--liquibase formatted sql
--

--
--changeset svn:11088 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11088_date_2023_01_07_time_23_24_38_issue_124_idx_session.sql
--

--

CREATE INDEX IF NOT EXISTS IDX_security_session_user_name
    ON security.session (user_name);

CREATE INDEX IF NOT EXISTS IDX_security_session_valid_time
    ON security.session (valid_time);

--
--rollback DROP INDEX IF EXISTS security.IDX_security_session_valid_time; DROP INDEX IF EXISTS security.IDX_security_session_user_name;

