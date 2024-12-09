--liquibase formatted sql
--

--
--changeset 18578179:11090 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11090_date_2023_01_24_time_12_11_53_issues_158_dictionary_language_create_language.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.language_seq START 1;

CREATE TABLE IF NOT EXISTS dictionary.language (
    id            BIGINT  PRIMARY KEY          NOT NULL DEFAULT nextval('dictionary.language_seq'),
    language      VARCHAR(256)                 NOT NULL UNIQUE,
    user_name     VARCHAR(64)                  NOT NULL,
      CONSTRAINT  FK_1706_dictionary_language_security_user_name
      FOREIGN KEY (user_name)
      REFERENCES  security.user_name (user_name)
        ON DELETE CASCADE ON UPDATE CASCADE,
    create_time   TIMESTAMP WITHOUT TIME ZONE  NOT NULL DEFAULT now(),
    update_time   TIMESTAMP WITHOUT TIME ZONE           DEFAULT now(),
    enabled       BOOLEAN                               DEFAULT false,
    visible       BOOLEAN                               DEFAULT true,
    flags         INT                          NOT NULL DEFAULT 0
    );

--
--rollback DROP TABLE IF EXISTS dictionary.language; DROP SEQUENCE IF EXISTS dictionary.language_seq;

