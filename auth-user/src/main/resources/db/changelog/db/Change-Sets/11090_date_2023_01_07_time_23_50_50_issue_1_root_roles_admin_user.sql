--liquibase formatted sql
--

--
--changeset vnsk:11090 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11090_date_2023_01_07_time_23_50_50_issue_1_root_roles_admin_user.sql
--

--
INSERT INTO security.user_has_roles (user_name, role) VALUES ('root', 'ADMIN'), ('root', 'USER');

--
--rollback DELETE FROM security.user_has_roles WHERE user_name = 'root' AND role = 'USER';
--rollback DELETE FROM security.user_has_roles WHERE user_name = 'root' AND role = 'ADMIN';
