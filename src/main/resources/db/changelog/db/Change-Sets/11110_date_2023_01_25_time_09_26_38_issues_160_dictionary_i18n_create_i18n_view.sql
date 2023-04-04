--liquibase formatted sql
--

--
--changeset 18578179:11110 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11110_date_2023_01_25_time_09_26_38_issues_160_dictionary_i18n_create_i18n_view.sql
--

--
DROP VIEW IF EXISTS dictionary.i18n_view;
CREATE OR REPLACE VIEW dictionary.i18n_view AS
  SELECT I18n.*, Language.language
    FROM dictionary.i18n I18n
         LEFT JOIN dictionary.language Language
           ON I18n.language_id = Language.id;
;

--
--rollback DROP VIEW IF EXISTS dictionary.i18n_view;
