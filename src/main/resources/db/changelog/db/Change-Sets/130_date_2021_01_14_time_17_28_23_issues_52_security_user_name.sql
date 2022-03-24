--liquibase formatted sql
--

-- This file was last modified at 2022.03.23 22:47 by Victor N. Skurikhin.
-- This is free and unencumbered software released into the public domain.
-- For more information, please refer to <http://unlicense.org>
-- 130_date_2021_01_14_time_17_28_23_issues_52_security_user_name.sql

--
--changeset a18578179:130 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/130_date_2021_01_14_time_17_28_23_issues_52_security_user_name.sql
--

--
CREATE TABLE IF NOT EXISTS security.user_name (
  user_name     VARCHAR(64) PRIMARY KEY     NOT NULL,
  id            UUID                        NOT NULL    DEFAULT pg_catalog.uuid_generate_v4()
                CONSTRAINT UC_3109_security_id_must_be_unique UNIQUE,
  password      VARCHAR(1024)               NOT NULL,
  create_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL    DEFAULT now(),
  update_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL    DEFAULT now(),
  enabled       BOOLEAN                                 DEFAULT true,
  visible       BOOLEAN                                 DEFAULT true,
  flags         INT
);
--

--
--rollback DROP TABLE IF EXISTS security.user_name;
