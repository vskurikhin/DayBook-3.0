--liquibase formatted sql
--

--
--changeset svn:11120 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11120_date_2023_09_07_time_17_15_31_issues_179_create_setting.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.setting_seq START 1;

CREATE TABLE IF NOT EXISTS dictionary.setting (
    id            BIGINT  PRIMARY KEY  NOT NULL  DEFAULT nextval('dictionary.setting_seq'),
    variable      VARCHAR(1024) NOT NULL UNIQUE,
    value         VARCHAR(10485760),
    value_type_id BIGINT NOT NULL,
      CONSTRAINT  FK_5014_dictionary_setting_value_type_id
      FOREIGN KEY (value_type_id)
      REFERENCES  dictionary.value_type (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    user_name     VARCHAR(64)                  NOT NULL,
      CONSTRAINT  FK_6109_dictionary_setting_security_user_name
      FOREIGN KEY (user_name)
      REFERENCES  security.user_name (user_name)
        ON DELETE CASCADE ON UPDATE CASCADE,
    create_time   TIMESTAMP WITHOUT TIME ZONE  NOT NULL   DEFAULT now(),
    update_time   TIMESTAMP WITHOUT TIME ZONE             DEFAULT now(),
    enabled       BOOLEAN                                 DEFAULT false,
    visible       BOOLEAN                                 DEFAULT true,
    flags         INT                          NOT NULL   DEFAULT 0
    );

--
--rollback DROP TABLE IF EXISTS dictionary.setting; DROP SEQUENCE IF EXISTS dictionary.setting_seq;

