--liquibase formatted sql
--

--
--changeset vnsk:11164 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11164_date_2024_05_23_time_19_59_15_205_issue_triggers_json_records.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_db_json_records ON db.json_records;
CREATE TRIGGER update_create_time_db_json_records
    BEFORE INSERT
    ON db.json_records
    FOR EACH ROW
EXECUTE FUNCTION db.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_db_json_records ON db.json_records;
CREATE TRIGGER update_update_time_db_json_records
    BEFORE UPDATE
    ON db.json_records
    FOR EACH ROW
EXECUTE FUNCTION db.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_db_json_records ON db.json_records;
--rollback DROP TRIGGER IF EXISTS update_create_time_db_json_records ON db.json_records;
