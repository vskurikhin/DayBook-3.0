/*
 * This file was last modified at 2023.09.07 16:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 11116_date_2023_09_07_time_14_40_01_issues_180_idx_value_type.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset svn:11116 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11116_date_2023_09_07_time_14_40_01_issues_180_idx_value_type.sql
--

--

CREATE INDEX IF NOT EXISTS IDX_dictionary_value_type_user_name
    ON dictionary.value_type (user_name);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_value_type_user_name;

