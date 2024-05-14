--liquibase formatted sql
--

--
--changeset svn:11139 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11139_date_2023_09_11_time_13_30_00_issues_183_recreate_setting_view.sql
--

DROP VIEW IF EXISTS dictionary.setting_view;
CREATE OR REPLACE VIEW dictionary.setting_view AS
SELECT Setting.*, ValueType.value_type
  FROM dictionary.setting Setting
         LEFT JOIN dictionary.value_type ValueType
                   ON Setting.value_type_id = ValueType.id
;

--
--rollback DROP VIEW IF EXISTS dictionary.setting_view;
