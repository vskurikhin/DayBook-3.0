--liquibase formatted sql
--

--
--changeset vnsk:11088 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11088_date_2023_01_07_time_23_30_30_issue_1_insert_root.sql
--

--
INSERT INTO security.user_name (user_name, password) VALUES ('root', 'JjNAmobTGeuZ+P3iA4TFKZRduhFNd5tyGR+coMU0o0o=');

--
--rollback DELETE FROM security.user_name WHERE user_name = 'root';
