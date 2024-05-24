--liquibase formatted sql
--

--
--changeset vnsk:11168 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11168_date_2024_05_23_time_19_59_32_205_issue_idx_json_records.sql
--

--
CREATE INDEX IF NOT EXISTS IDX_db_json_records_user_name
    ON db.json_records (user_name);

--
--rollback DROP INDEX IF EXISTS db.IDX_db_json_records_user_name;
