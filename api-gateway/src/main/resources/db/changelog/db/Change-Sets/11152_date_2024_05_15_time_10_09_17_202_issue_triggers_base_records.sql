--liquibase formatted sql
--

--
--changeset vnsk:11152 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11152_date_2024_05_15_time_10_09_17_202_issue_triggers_base_records.sql
--

--
DROP TRIGGER IF EXISTS update_create_time_db_base_records ON db.base_records;
CREATE TRIGGER update_create_time_db_base_records
    BEFORE INSERT
    ON db.base_records
    FOR EACH ROW
EXECUTE FUNCTION db.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_db_base_records ON db.base_records;
CREATE TRIGGER update_update_time_db_base_records
    BEFORE UPDATE
    ON db.base_records
    FOR EACH ROW
EXECUTE FUNCTION db.update_update_time();

DROP TRIGGER IF EXISTS set_parent_if_null_db_base_records ON db.base_records;
CREATE TRIGGER set_parent_if_null_db_base_records
    BEFORE INSERT
    ON db.base_records
    FOR EACH ROW
EXECUTE FUNCTION db.set_parent_if_null();

DROP TRIGGER IF EXISTS old_parent_if_null_db_base_records ON db.base_records;
CREATE TRIGGER old_parent_if_null_db_base_records
    BEFORE UPDATE
    ON db.base_records
    FOR EACH ROW
EXECUTE FUNCTION db.old_parent_if_null();

--
--rollback DROP TRIGGER IF EXISTS old_parent_if_null_db_base_records ON db.base_records;
--rollback DROP TRIGGER IF EXISTS set_parent_if_null_db_base_records ON db.base_records;
--rollback DROP TRIGGER IF EXISTS update_create_time_db_base_records ON db.base_records;
--rollback DROP TRIGGER IF EXISTS update_create_time_db_base_records ON db.base_records;
