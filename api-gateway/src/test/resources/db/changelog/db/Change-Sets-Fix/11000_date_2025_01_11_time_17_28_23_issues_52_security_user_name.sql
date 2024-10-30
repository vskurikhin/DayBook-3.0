--liquibase formatted sql
--

--
--changeset a18578179:11000 failOnError:true logicalFilePath:src/test/resources/db/changelog/db/Change-Sets-Fix/11000_date_2025_01_11_time_17_28_23_issues_52_security_user_name.sql
--

--
CREATE TABLE IF NOT EXISTS security.user_name
(
    user_name   VARCHAR(64) PRIMARY KEY     NOT NULL,
    id          UUID                        NOT NULL DEFAULT pg_catalog.uuid_generate_v4()
        CONSTRAINT UC_3109_security_id_must_be_unique UNIQUE,
    password    VARCHAR(1024)               NOT NULL,
    create_time TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    update_time TIMESTAMP WITHOUT TIME ZONE          DEFAULT now(),
    enabled     BOOLEAN                     NOT NULL DEFAULT true,
    visible     BOOLEAN                     NOT NULL DEFAULT true,
    flags       INT                         NOT NULL DEFAULT 0
);


CREATE INDEX IF NOT EXISTS IDX_security_user_name_id
    ON security.user_name (id);

--
--rollback DROP INDEX IF EXISTS security.IDX_security_user_name_id; DROP TABLE IF EXISTS security.user_name;
