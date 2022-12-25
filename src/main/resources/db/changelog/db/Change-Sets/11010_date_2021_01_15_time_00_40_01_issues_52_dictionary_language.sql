--liquibase formatted sql
--

--
--changeset a18578179:11010 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11010_date_2021_01_15_time_00_40_01_issues_52_dictionary_language.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.language_seq START 1;

CREATE TABLE IF NOT EXISTS dictionary.language (
  id            BIGINT  PRIMARY KEY  NOT NULL  DEFAULT nextval('dictionary.language_seq'),
  language      VARCHAR(256),
  user_name     VARCHAR(64),
                CONSTRAINT FK_4792_dictionary_language_security_user_name
                FOREIGN KEY (user_name)
                REFERENCES  security.user_name (user_name)
                ON DELETE CASCADE ON UPDATE CASCADE,
  create_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL    DEFAULT now(),
  update_time   TIMESTAMP WITHOUT TIME ZONE             DEFAULT now(),
  enabled       BOOLEAN                     NOT NULL    DEFAULT false,
  visible       BOOLEAN                     NOT NULL    DEFAULT true,
  flags         INT                         NOT NULL    DEFAULT 0
);

CREATE INDEX IF NOT EXISTS IDX_dictionary_language_user_name
    ON dictionary.language (user_name);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_language_user_name; DROP TABLE IF EXISTS dictionary.language; DROP SEQUENCE IF EXISTS dictionary.language_seq;
