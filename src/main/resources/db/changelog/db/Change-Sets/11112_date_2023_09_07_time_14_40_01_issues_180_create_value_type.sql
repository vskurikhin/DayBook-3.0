/*
 * This file was last modified at 2023.09.07 16:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 11112_date_2023_09_07_time_14_40_01_issues_180_create_value_type.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset svn:11112 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11112_date_2023_09_07_time_14_40_01_issues_180_create_value_type.sql
--

--
CREATE SEQUENCE IF NOT EXISTS dictionary.value_type_seq START 1;

CREATE TABLE IF NOT EXISTS dictionary.value_type
(
    id          BIGINT PRIMARY KEY          NOT NULL DEFAULT nextval('dictionary.value_type_seq'),
    value_type  VARCHAR(1024)               NOT NULL UNIQUE,
    user_name   VARCHAR(64),
    CONSTRAINT FK_1981_dictionary_value_type_security_user_name
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
--rollback DROP TABLE IF EXISTS dictionary.value_type; DROP SEQUENCE IF EXISTS dictionary.value_type_seq;

