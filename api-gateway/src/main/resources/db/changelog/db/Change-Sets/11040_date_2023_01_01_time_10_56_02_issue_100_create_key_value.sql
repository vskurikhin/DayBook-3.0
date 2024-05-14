--liquibase formatted sql
--

--
--changeset svn:11040 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11040_date_2023_01_01_time_10_56_02_issue_100_create_key_value.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.key_value_seq START 1;

CREATE TABLE IF NOT EXISTS dictionary.key_value (
    id            UUID  PRIMARY KEY    NOT NULL  DEFAULT pg_catalog.uuid_generate_v4(),
    key           NUMERIC(64)          NOT NULL  UNIQUE,
    value         JSONB,
    user_name     VARCHAR(64),
      CONSTRAINT  FK_5004_dictionary_key_value_security_user_name
      FOREIGN KEY (user_name)
      REFERENCES  security.user_name (user_name)
        ON DELETE CASCADE ON UPDATE CASCADE,
    create_time   TIMESTAMP WITHOUT TIME ZONE  NOT NULL   DEFAULT now(),
    update_time   TIMESTAMP WITHOUT TIME ZONE             DEFAULT now(),
    enabled       BOOLEAN                                 DEFAULT true,
    visible       BOOLEAN                                 DEFAULT true,
    flags         INT                          NOT NULL   DEFAULT 0
    );

--
--rollback DROP TABLE IF EXISTS dictionary.key_value; DROP SEQUENCE IF EXISTS dictionary.key_value_seq;

