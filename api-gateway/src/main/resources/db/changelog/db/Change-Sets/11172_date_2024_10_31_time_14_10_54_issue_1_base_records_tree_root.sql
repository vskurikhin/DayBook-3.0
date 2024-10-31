--liquibase formatted sql
--

--
--changeset vnsk:11172 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11172_date_2024_10_31_time_14_10_54_issue_1_base_records_tree_root.sql
--

--
INSERT INTO db.base_records (id, parent_id, user_name) VALUES ('00000000-0000-0000-0000-000000000000', '00000000-0000-0000-0000-000000000000', 'root');

--
--rollback DELETE FROM db.base_records WHERE id = '00000000-0000-0000-0000-000000000000';
