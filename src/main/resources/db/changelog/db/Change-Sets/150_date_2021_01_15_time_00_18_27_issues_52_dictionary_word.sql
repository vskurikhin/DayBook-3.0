/*
 * This file was last modified at 2022.03.23 22:47 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 150_date_2021_01_15_time_00_18_27_issues_52_dictionary_word.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:150 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/150_date_2021_01_15_time_00_18_27_issues_52_dictionary_word.sql
--

--
CREATE TABLE IF NOT EXISTS dictionary.word (
  word          VARCHAR(256) PRIMARY KEY    NOT NULL,
  user_name     VARCHAR(64),
                CONSTRAINT FK_7854_dictionary_word_security_user_name
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
--rollback DROP TABLE IF EXISTS dictionary.word;
