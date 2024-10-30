--liquibase formatted sql
--

--
--changeset a18578179:11002 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11002_date_2021_01_15_time_00_10_10_issue_52_dictionary_codifier.sql
--

--
CREATE TABLE IF NOT EXISTS dictionary.codifier (
  code          VARCHAR(64) PRIMARY KEY     NOT NULL,
  value         VARCHAR(10485760),
  user_name     VARCHAR(64)                 NOT NULL,
                CONSTRAINT FK_84d3_dictionary_codifier_security_user_name
                FOREIGN KEY (user_name)
                REFERENCES  security.user_name (user_name)
                ON DELETE CASCADE ON UPDATE CASCADE,
  create_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL    DEFAULT now(),
  update_time   TIMESTAMP WITHOUT TIME ZONE             DEFAULT now(),
  enabled       BOOLEAN                     NOT NULL    DEFAULT true,
  visible       BOOLEAN                     NOT NULL    DEFAULT true,
  flags         INT                         NOT NULL    DEFAULT 0
);

CREATE INDEX IF NOT EXISTS IDX_dictionary_codifier_user_name
    ON dictionary.codifier (user_name);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_codifier_user_name; DROP TABLE IF EXISTS dictionary.codifier;

