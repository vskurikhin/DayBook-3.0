--liquibase formatted sql
--

--
--changeset svn:11064 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11064_date_2023_01_05_time_17_57_27_issue_122_create_user_has_roles.sql
--

--
CREATE SEQUENCE IF NOT EXISTS security.user_has_roles_seq START 1;

CREATE TABLE IF NOT EXISTS security.user_has_roles
(
    id              BIGINT PRIMARY KEY          NOT NULL DEFAULT nextval('security.user_has_roles_seq'),
    user_name       VARCHAR(64),
    CONSTRAINT FK_2751_security_user_has_roles_security_user_name
        FOREIGN KEY (user_name)
            REFERENCES security.user_name (user_name)
            ON DELETE CASCADE ON UPDATE CASCADE,
    role            VARCHAR(64),
    CONSTRAINT FK_2751_security_user_has_roles_security_role
        FOREIGN KEY (role)
            REFERENCES security.role (role)
            ON DELETE CASCADE ON UPDATE CASCADE,
    create_time     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    update_time     TIMESTAMP WITHOUT TIME ZONE          DEFAULT now(),
    enabled         BOOLEAN                              DEFAULT false,
    local_change    BOOLEAN                     NOT NULL DEFAULT true,
    visible         BOOLEAN                              DEFAULT true,
    flags           INT                         NOT NULL DEFAULT 0
);

--
--rollback DROP TABLE IF EXISTS security.user_has_roles; DROP SEQUENCE IF EXISTS security.user_has_roles_seq;

