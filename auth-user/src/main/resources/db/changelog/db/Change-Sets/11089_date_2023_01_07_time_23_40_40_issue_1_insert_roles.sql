--liquibase formatted sql
--

--
--changeset vnsk:11089 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11089_date_2023_01_07_time_23_40_40_issue_1_insert_roles.sql
--

--
INSERT INTO security.role (role, user_name) VALUES ('ADMIN', 'root'), ('GUEST', 'root'), ('USER', 'root');

--
--rollback DELETE FROM security.role WHERE role IN  ('ADMIN', 'GUEST', 'USER');
