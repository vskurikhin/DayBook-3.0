--liquibase formatted sql
--

--
--changeset svn:11138 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11138_date_2023_09_10_time_20_31_05_alter_table_setting_stanza_id_idx_setting_stanza_id_issues_183.sql
--

--
ALTER TABLE dictionary.setting
    ADD COLUMN stanza_id BIGINT NOT NULL DEFAULT 0;

ALTER TABLE IF EXISTS dictionary.setting
    ADD CONSTRAINT FK_0386_dictionary_setting_stanza_id
        FOREIGN KEY (stanza_id) REFERENCES dictionary.stanza
            ON DELETE CASCADE ON UPDATE CASCADE;

CREATE INDEX IF NOT EXISTS IDX_dictionary_setting_stanza_id
    ON dictionary.setting (stanza_id);

--
--rollback DROP INDEX IF EXISTS dictionary.IDX_dictionary_setting_stanza_id;
--rollback DROP VIEW IF EXISTS dictionary.setting_view;
--rollback ALTER TABLE IF EXISTS dictionary.setting DROP CONSTRAINT IF EXISTS FK_0386_dictionary_setting_stanza_id;
--rollback ALTER TABLE IF EXISTS dictionary.setting DROP COLUMN IF EXISTS stanza_id;
--rollback CREATE OR REPLACE VIEW dictionary.setting_view AS SELECT Setting.*, ValueType.value_type FROM dictionary.setting Setting LEFT JOIN dictionary.value_type ValueType ON Setting.value_type_id = ValueType.id;
