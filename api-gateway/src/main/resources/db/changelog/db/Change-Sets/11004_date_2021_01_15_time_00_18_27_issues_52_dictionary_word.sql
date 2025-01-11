--liquibase formatted sql
--

--
--changeset a18578179:11004 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11004_date_2021_01_15_time_00_18_27_issues_52_dictionary_word.sql
--

--
CREATE TABLE IF NOT EXISTS dictionary.word (
  word          VARCHAR(256) PRIMARY KEY    NOT NULL,
  user_name     VARCHAR(64)                 NOT NULL,
                CONSTRAINT FK_7854_dictionary_word_security_user_name
                FOREIGN KEY (user_name)
                REFERENCES  security.user_name (user_name)
                ON DELETE CASCADE ON UPDATE CASCADE,
  create_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL    DEFAULT now(),
  update_time   TIMESTAMP WITHOUT TIME ZONE             DEFAULT now(),
  enabled       BOOLEAN                     NOT NULL    DEFAULT true,
  visible       BOOLEAN                     NOT NULL    DEFAULT true,
  flags         INT                         NOT NULL    DEFAULT 0
);

CREATE INDEX IF NOT EXISTS IDX_dictionary_word_user_name
    ON dictionary.word (user_name);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_word_user_name; DROP TABLE IF EXISTS dictionary.word;
