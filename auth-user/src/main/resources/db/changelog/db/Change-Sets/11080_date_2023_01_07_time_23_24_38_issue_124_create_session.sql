--liquibase formatted sql
--

--
--changeset svn:11080 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11080_date_2023_01_07_time_23_24_38_issue_124_create_session.sql
--

--
CREATE TABLE IF NOT EXISTS security.session
(
    id          UUID PRIMARY KEY            NOT NULL DEFAULT pg_catalog.uuid_generate_v4(),
    user_name   VARCHAR(64),
    CONSTRAINT FK_8053_security_session_security_user_name
        FOREIGN KEY (user_name)
            REFERENCES security.user_name (user_name)
            ON DELETE CASCADE ON UPDATE CASCADE,
    roles       TEXT[]                      NOT NULL,
    valid_time  TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    create_time TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    update_time TIMESTAMP WITHOUT TIME ZONE          DEFAULT now(),
    enabled     BOOLEAN                              DEFAULT false,
    visible     BOOLEAN                              DEFAULT true,
    flags       INT                         NOT NULL DEFAULT 0
);

--
--rollback DROP TABLE IF EXISTS security.session;

