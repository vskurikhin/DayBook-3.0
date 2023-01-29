--liquibase formatted sql
--

--
--changeset 18578179:11098 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11098_date_2023_01_25_time_09_26_35_issues_160_dictionary_i18n_create_i18n.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.i18n_seq START 1;

CREATE TABLE IF NOT EXISTS dictionary.i18n
(
    id          BIGINT PRIMARY KEY          NOT NULL DEFAULT nextval('dictionary.i18n_seq'),
    language_id BIGINT                      NOT NULL,
      CONSTRAINT FK_dictionary_i18n_language_id_7344
        FOREIGN KEY (language_id)
        REFERENCES dictionary.language (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    message     VARCHAR(10485760)           NOT NULL,
      CONSTRAINT UQ_1553_dictionary_i18n_uniq_language_id_message
        UNIQUE (language_id, message),
    translation VARCHAR(10485760),
    user_name   VARCHAR(64),
      CONSTRAINT FK_3155_dictionary_i18n_security_user_name
        FOREIGN KEY (user_name)
        REFERENCES security.user_name (user_name)
        ON DELETE CASCADE ON UPDATE CASCADE,
    create_time TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    update_time TIMESTAMP WITHOUT TIME ZONE          DEFAULT now(),
    enabled     BOOLEAN                              DEFAULT false,
    visible     BOOLEAN                              DEFAULT true,
    flags       INT                         NOT NULL DEFAULT 0
);

--
--rollback DROP TABLE IF EXISTS dictionary.i18n; DROP SEQUENCE IF EXISTS dictionary.i18n_seq;

