--liquibase formatted sql
--

--
--changeset svn:11130 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11130_date_2023_09_10_time_14_39_18_issues_183_create_stanza.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.stanza_seq START 1;

CREATE TABLE IF NOT EXISTS dictionary.stanza (
    id            BIGINT  PRIMARY KEY  NOT NULL  DEFAULT nextval('dictionary.stanza_seq'),
    name          VARCHAR(256) NOT NULL,
    description   VARCHAR(65536),
    parent_id     BIGINT NOT NULL,
      CONSTRAINT  FK_9563_dictionary_stanza_parent_id
      FOREIGN KEY (parent_id)
      REFERENCES  dictionary.stanza (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    user_name     VARCHAR(64)                   NOT NULL,
      CONSTRAINT  FK_5911_dictionary_stanza_security_user_name
      FOREIGN KEY (user_name)
      REFERENCES  security.user_name (user_name)
        ON DELETE CASCADE ON UPDATE CASCADE,
    create_time   TIMESTAMP WITHOUT TIME ZONE   NOT NULL    DEFAULT now(),
    update_time   TIMESTAMP WITHOUT TIME ZONE               DEFAULT now(),
    enabled       BOOLEAN                                   DEFAULT false,
    local_change  BOOLEAN                       NOT NULL    DEFAULT true,
    visible       BOOLEAN                                   DEFAULT true,
    flags         INT                           NOT NULL    DEFAULT 0
    );

--
--rollback DROP TABLE IF EXISTS dictionary.stanza; DROP SEQUENCE IF EXISTS dictionary.stanza_seq;

