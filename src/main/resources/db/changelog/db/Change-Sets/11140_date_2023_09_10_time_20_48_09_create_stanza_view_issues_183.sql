--liquibase formatted sql
--

--
--changeset svn:11140 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11140_date_2023_09_10_time_20_48_09_create_stanza_view_issues_183.sql
--

--
DROP VIEW IF EXISTS dictionary.stanza_view;
CREATE OR REPLACE VIEW dictionary.stanza_view AS
SELECT Stanza.*,
       Parent.name AS parent_name,
       Parent.description AS parent_description,
       Parent.parent_id AS parent_parent_id,
       Parent.user_name AS parent_user_name,
       Parent.create_time AS parent_create_time,
       Parent.update_time AS parent_update_time,
       Parent.enabled AS parent_enabled,
       Parent.visible AS parent_visible,
       Parent.flags AS parent_flags,
       json_agg(Setting.*) AS settings_json
FROM dictionary.stanza Stanza
         LEFT JOIN dictionary.setting_view Setting
                   ON Stanza.id = Setting.stanza_id
         LEFT JOIN dictionary.stanza Parent
                   ON Stanza.parent_id = Parent.id
GROUP BY Stanza.id,
         Stanza.name,
         Stanza.description,
         Stanza.parent_id,
         Stanza.user_name,
         Stanza.create_time,
         Stanza.update_time,
         Stanza.enabled,
         Stanza.visible,
         Stanza.flags,
         Parent.name,
         Parent.description,
         Parent.parent_id,
         Parent.user_name,
         Parent.create_time,
         Parent.update_time,
         Parent.enabled,
         Parent.visible,
         Parent.flags,
         Setting.stanza_id
;

--
--rollback DROP VIEW IF EXISTS dictionary.stanza_view;
