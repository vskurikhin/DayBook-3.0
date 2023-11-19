--liquibase formatted sql
--

--
--changeset svn:11134 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11134_date_2023_09_10_time_14_39_18_issues_183_idx_stanza.sql
--

--

CREATE INDEX IF NOT EXISTS IDX_dictionary_stanza_user_name
    ON dictionary.stanza (user_name);

CREATE INDEX IF NOT EXISTS IDX_dictionary_stanza_parent_id
    ON dictionary.stanza (parent_id);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_stanza_parent_id;
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_stanza_user_name;

