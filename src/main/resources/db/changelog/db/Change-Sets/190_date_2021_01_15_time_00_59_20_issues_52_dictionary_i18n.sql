--liquibase formatted sql
--

-- This file was last modified at 2022.03.23 22:47 by Victor N. Skurikhin.
-- This is free and unencumbered software released into the public domain.
-- For more information, please refer to <http://unlicense.org>
-- 190_date_2021_01_15_time_00_59_20_issues_52_dictionary_i18n.sql

--
--changeset a18578179:190 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/190_date_2021_01_15_time_00_59_20_issues_52_dictionary_i18n.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.i18n_seq START 1;

CREATE TABLE IF NOT EXISTS dictionary.i18n (
  id            BIGINT  PRIMARY KEY  NOT NULL  DEFAULT nextval('dictionary.i18n_seq'),
  language_id   BIGINT  NOT NULL,
                CONSTRAINT FK_dictionary_i18n_language_id_7344
                FOREIGN KEY (language_id)
                REFERENCES  dictionary.language (id)
                ON DELETE CASCADE ON UPDATE CASCADE,
  message       VARCHAR(10485760),
  translation   VARCHAR(10485760),
  user_name     VARCHAR(64),
                CONSTRAINT FK_4b19_dictionary_i18n_security_user_name
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
--rollback DROP TABLE IF EXISTS dictionary.i18n; DROP SEQUENCE IF EXISTS dictionary.i18n_seq;
