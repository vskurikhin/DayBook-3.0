--liquibase formatted sql
--

--
--changeset svn:11016 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11016_date_2022_12_29_time_17_38_29_issue_96_create_value_type.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.value_type_seq START 1;

CREATE TABLE IF NOT EXISTS dictionary.value_type (
    id            BIGINT  PRIMARY KEY  NOT NULL  DEFAULT nextval('dictionary.value_type_seq'),
    value_type    VARCHAR(1024) NOT NULL UNIQUE,
    user_name     VARCHAR(64),
      CONSTRAINT  FK_2998_dictionary_value_type_security_user_name
      FOREIGN KEY (user_name)
      REFERENCES  security.user_name (user_name)
        ON DELETE CASCADE ON UPDATE CASCADE,
    create_time   TIMESTAMP WITHOUT TIME ZONE  NOT NULL   DEFAULT now(),
    update_time   TIMESTAMP WITHOUT TIME ZONE             DEFAULT now(),
    enabled       BOOLEAN                                 DEFAULT false,
    visible       BOOLEAN                                 DEFAULT true,
    flags         INT
    );

--
--rollback DROP TABLE IF EXISTS dictionary.value_type; DROP SEQUENCE IF EXISTS dictionary.value_type_seq;

