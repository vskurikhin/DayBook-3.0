/*
 * This file was last modified at 2022.03.24 13:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 170_date_2021_01_15_time_00_35_14_issues_52_dictionary_tag_label.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:170 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/170_date_2021_01_15_time_00_35_14_issues_52_dictionary_tag_label.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.tag_label_seq START 1;

CREATE OR REPLACE FUNCTION dictionary.next_val_tag_label_seq() RETURNS TEXT
    IMMUTABLE LANGUAGE SQL
AS $$ SELECT lpad(to_hex(nextval('dictionary.tag_label_seq')), 16, '0') $$;

CREATE TABLE dictionary.tag_label (
  id            CHAR(16)  PRIMARY KEY  NOT NULL  DEFAULT dictionary.next_val_tag_label_seq(),
  label         VARCHAR(128)           NOT NULL
                CONSTRAINT UC_85b5_dictionary_tag_must_be_different UNIQUE,
  user_name     VARCHAR(64),
                CONSTRAINT FK_4783_dictionary_tag_label_security_user_name
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
--rollback DROP TABLE IF EXISTS dictionary.tag_label; DROP FUNCTION IF EXISTS dictionary.next_val_tag_label_seq; DROP SEQUENCE IF EXISTS dictionary.tag_label_seq;
