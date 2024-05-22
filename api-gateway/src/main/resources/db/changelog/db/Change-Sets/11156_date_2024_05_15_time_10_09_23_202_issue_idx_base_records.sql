--liquibase formatted sql
--

--
--changeset vnsk:11156 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11156_date_2024_05_15_time_10_09_23_202_issue_idx_base_records.sql
--

--
CREATE INDEX IF NOT EXISTS IDX_db_base_records_user_name
    ON db.base_records (user_name);

--
--rollback DROP INDEX IF EXISTS db.IDX_db_base_records_user_name;
