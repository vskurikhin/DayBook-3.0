--liquibase formatted sql
--

--
--changeset svn:11136 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11136_date_2023_09_10_time_20_28_39_insert_root_stanza_issues_183.sql
--

--
INSERT INTO dictionary.stanza (id, name, parent_id, enabled) VALUES (0, '00000000-0000-0000-0000-000000000000', 0, true);

--
--rollback DELETE FROM dictionary.stanza WHERE id = 0 AND name = '00000000-0000-0000-0000-000000000000' AND parent_id = 0;
