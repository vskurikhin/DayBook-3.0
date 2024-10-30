--liquibase formatted sql
--

--
--changeset svn:11072 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11072_date_2023_01_05_time_17_57_27_issue_122_idx_user_has_roles.sql
--

--

CREATE INDEX IF NOT EXISTS IDX_security_user_has_roles_user_name
    ON security.user_has_roles (user_name);

CREATE UNIQUE INDEX IF NOT EXISTS IDX_security_user_has_roles_user_name_role
    ON security.user_has_roles (user_name, role);

ALTER TABLE security.user_has_roles
    ADD CONSTRAINT UNQ_2751_security_user_has_roles_user_name_role
        UNIQUE USING INDEX IDX_security_user_has_roles_user_name_role;

--
--rollback ALTER TABLE IF EXISTS security.user_has_roles DROP CONSTRAINT UNQ_2751_security_user_has_roles_user_name_role; DROP INDEX IF EXISTS security.IDX_security_user_has_roles_user_name_role; DROP INDEX IF EXISTS security.IDX_security_user_has_roles_user_name;

