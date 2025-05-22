--liquibase formatted sql
--

--
--changeset svn:11052 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11052_date_2023_01_04_time_00_26_03_issue_114_create_role.sql
--

--
CREATE TABLE IF NOT EXISTS security.role
(
    role            VARCHAR(64) PRIMARY KEY     NOT NULL,
    id              UUID                        NOT NULL DEFAULT pg_catalog.uuid_generate_v4()
        CONSTRAINT UC_8532_security_id_must_be_unique UNIQUE,
    description     VARCHAR(4096),
    user_name       VARCHAR(64)                 NOT NULL,
    CONSTRAINT FK_8952_security_role_security_user_name
        FOREIGN KEY (user_name)
            REFERENCES  security.user_name (user_name)
            ON DELETE CASCADE ON UPDATE CASCADE,
    create_time     TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    update_time     TIMESTAMP WITHOUT TIME ZONE          DEFAULT now(),
    enabled         BOOLEAN                     NOT NULL DEFAULT true,
    local_change    BOOLEAN                     NOT NULL DEFAULT true,
    visible         BOOLEAN                     NOT NULL DEFAULT true,
    flags           INT                         NOT NULL DEFAULT 0
);

--
--rollback DROP TABLE IF EXISTS security.role;

