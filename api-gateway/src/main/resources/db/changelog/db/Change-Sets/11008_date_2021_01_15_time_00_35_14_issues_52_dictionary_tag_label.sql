--liquibase formatted sql
--

--
--changeset a18578179:11008  splitStatements:false failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11008_date_2021_01_15_time_00_35_14_issues_52_dictionary_tag_label.sql
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
  create_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL    DEFAULT now(),
  update_time   TIMESTAMP WITHOUT TIME ZONE             DEFAULT now(),
  enabled       BOOLEAN                     NOT NULL    DEFAULT true,
  visible       BOOLEAN                     NOT NULL    DEFAULT true,
  flags         INT                         NOT NULL    DEFAULT 0
);

CREATE INDEX IF NOT EXISTS IDX_dictionary_tag_label_user_name
    ON dictionary.tag_label (user_name);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_tag_label_user_name; DROP TABLE IF EXISTS dictionary.tag_label; DROP FUNCTION IF EXISTS dictionary.next_val_tag_label_seq; DROP SEQUENCE IF EXISTS dictionary.tag_label_seq;
