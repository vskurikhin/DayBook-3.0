--liquibase formatted sql
--

--
--changeset svn:11060 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11060_date_2023_01_04_time_00_26_03_issue_114_idx_role.sql
--

--

CREATE INDEX IF NOT EXISTS IDX_security_role_id
    ON security.role (id);

--
--rollback DROP INDEX IF EXISTS security.IDX_security_role_id;

