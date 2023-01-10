--liquibase formatted sql
--

--
--changeset svn:11014 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11014_date_2022_12_25_time_00_31_35_issue_55_dictionary_value_type.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.value_type_seq START 1;

CREATE TABLE IF NOT EXISTS dictionary.value_type
(
    id          BIGINT PRIMARY KEY          NOT NULL DEFAULT nextval('dictionary.value_type_seq'),
    value_type  VARCHAR(1024),
    user_name   VARCHAR(64),
    CONSTRAINT FK_5af5_dictionary_value_type_security_user_name
        FOREIGN KEY (user_name)
            REFERENCES security.user_name (user_name)
            ON DELETE CASCADE ON UPDATE CASCADE,
    create_time TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    update_time TIMESTAMP WITHOUT TIME ZONE          DEFAULT now(),
    enabled     BOOLEAN                              DEFAULT true,
    visible     BOOLEAN                              DEFAULT true,
    flags       INT
);

CREATE INDEX IF NOT EXISTS IDX_dictionary_value_type_user_name
    ON dictionary.value_type (user_name);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_value_type_user_name; DROP TABLE IF EXISTS dictionary.value_type; DROP SEQUENCE IF EXISTS dictionary.value_type_seq;