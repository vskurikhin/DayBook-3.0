--liquibase formatted sql
--

-- This file was last modified at 2022.03.23 22:47 by Victor N. Skurikhin.
-- This is free and unencumbered software released into the public domain.
-- For more information, please refer to <http://unlicense.org>
-- 140_date_2021_01_15_time_00_10_10_issue_52_dictionary_codifier.sql

--
--changeset a18578179:140 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/140_date_2021_01_15_time_00_10_10_issue_52_dictionary_codifier.sql
--

--
CREATE TABLE IF NOT EXISTS dictionary.codifier (
  code          VARCHAR(64) PRIMARY KEY     NOT NULL,
  value         VARCHAR(10485760),
  user_name     VARCHAR(64),
                CONSTRAINT FK_84d3_dictionary_codifier_security_user_name
                FOREIGN KEY (user_name)
                REFERENCES  security.user_name (user_name)
                ON DELETE CASCADE ON UPDATE CASCADE,
  create_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL    DEFAULT now(),
  update_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL    DEFAULT now(),
  enabled       BOOLEAN                                 DEFAULT false,
  visible       BOOLEAN                                 DEFAULT true,
  flags         INT
);
--

--
--rollback DROP TABLE IF EXISTS dictionary.codifier;
