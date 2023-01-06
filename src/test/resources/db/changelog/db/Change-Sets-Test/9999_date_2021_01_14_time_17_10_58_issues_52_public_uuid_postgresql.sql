/*
 * This file was last modified at 2022.12.24 21:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * 9999_date_2021_01_14_time_17_10_58_issues_52_public_uuid_postgresql.sql
 * $Id$
 */

--liquibase formatted sql
--

--
--changeset a18578179:9999 failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/9999_date_2021_01_14_time_17_10_58_issues_52_public_uuid_postgresql.sql
--

--
-- INSERT INTO security.user_name (user_name, password)
-- VALUES ('user1', 'pass1'), ('user2', 'pass2'), ('user3', 'pass3');
-- INSERT INTO security.role (role) VALUES ('GUEST'), ('ADMIN'), ('USER');
-- INSERT INTO security.user_has_roles (user_name, role) VALUES ('user1', 'ADMIN'), ('user1', 'USER'), ('user2', 'USER');
-- INSERT INTO security.user_has_roles (user_name, role) VALUES ('root', 'ADMIN'), ('root', 'USER'), ('root', 'GUEST');

--
--rollback DELETE FROM security.user_has_roles WHERE user_name = 'user1'; DELETE FROM security.user_has_roles WHERE user_name = 'user2'; DELETE FROM security.role WHERE role IN ('GUEST', 'ADMIN', 'USER'); DELETE FROM security.user_name WHERE user_name = 'user3'; DELETE FROM security.user_name WHERE user_name = 'user2'; DELETE FROM security.user_name WHERE user_name = 'user1';