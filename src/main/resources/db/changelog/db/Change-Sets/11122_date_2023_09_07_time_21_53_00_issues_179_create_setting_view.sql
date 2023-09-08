--liquibase formatted sql
--

--
--changeset svn:11120 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11122_date_2023_09_07_time_21_53_00_issues_179_create_setting_view.sql
--

DROP VIEW IF EXISTS dictionary.setting_view;
CREATE OR REPLACE VIEW dictionary.setting_view AS
SELECT Setting.*, ValueType.value_type
  FROM dictionary.setting Setting
         LEFT JOIN dictionary.value_type ValueType
                   ON Setting.value_type_id = ValueType.id;
;

--
--rollback DROP VIEW IF EXISTS dictionary.setting_view;
