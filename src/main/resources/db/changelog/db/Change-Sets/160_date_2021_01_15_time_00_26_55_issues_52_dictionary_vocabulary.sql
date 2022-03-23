/*
 * This file was last modified at 2022.03.23 22:47 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 160_date_2021_01_15_time_00_26_55_issues_52_dictionary_vocabulary.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:160 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/160_date_2021_01_15_time_00_26_55_issues_52_dictionary_vocabulary.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.vocabulary_seq START 1;

CREATE TABLE IF NOT EXISTS dictionary.vocabulary (
  id            BIGINT  PRIMARY KEY  NOT NULL  DEFAULT nextval('dictionary.vocabulary_seq'),
  word          VARCHAR(256) NOT NULL,
                CONSTRAINT FK_80e4_dictionary_vocabulary_to_word
                FOREIGN KEY (word)
                REFERENCES  dictionary.word (word)
                ON DELETE CASCADE ON UPDATE CASCADE,
  value         VARCHAR(10485760),
  user_name     VARCHAR(64),
                CONSTRAINT FK_92c6_dictionary_vocabulary_security_user_name
                FOREIGN KEY (user_name)
                REFERENCES  security.user_name (user_name)
                ON DELETE CASCADE ON UPDATE CASCADE,
  create_time   TIMESTAMP WITHOUT TIME ZONE  NOT NULL   DEFAULT now(),
  update_time   TIMESTAMP WITHOUT TIME ZONE  NOT NULL   DEFAULT now(),
  enabled       BOOLEAN                                 DEFAULT false,
  visible       BOOLEAN                                 DEFAULT true,
  flags         INT
);
--

--
--rollback DROP TABLE IF EXISTS dictionary.vocabulary; DROP SEQUENCE IF EXISTS dictionary.vocabulary_seq;
