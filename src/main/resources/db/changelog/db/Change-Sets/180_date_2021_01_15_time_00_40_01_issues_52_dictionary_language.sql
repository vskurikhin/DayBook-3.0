/*
 * This file was last modified at 2022.03.24 13:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 180_date_2021_01_15_time_00_40_01_issues_52_dictionary_language.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:180 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/180_date_2021_01_15_time_00_40_01_issues_52_dictionary_language.sql
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
  create_time   TIMESTAMP WITHOUT TIME ZONE  NOT NULL   DEFAULT now(),
  update_time   TIMESTAMP WITHOUT TIME ZONE  NOT NULL   DEFAULT now(),
  enabled       BOOLEAN                                 DEFAULT true,
  visible       BOOLEAN                                 DEFAULT true,
  flags         INT
);
--

--
--rollback DROP TABLE IF EXISTS dictionary.language; DROP SEQUENCE IF EXISTS dictionary.language_seq;
