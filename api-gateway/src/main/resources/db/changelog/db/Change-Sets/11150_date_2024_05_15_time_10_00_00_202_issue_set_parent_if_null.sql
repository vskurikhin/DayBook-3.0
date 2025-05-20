--liquibase formatted sql
--

--
--changeset vnsk:11150 endDelimiter:$$ splitStatements:false failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11150_date_2024_05_15_time_10_00_00_202_issue_set_parent_if_null.sql
--

--

CREATE OR REPLACE FUNCTION db.set_parent_if_null() RETURNS TRIGGER LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.parent_id IS NULL THEN
       NEW.parent_id = NEW.id;
    END IF;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION db.old_parent_if_null() RETURNS TRIGGER LANGUAGE plpgsql
AS $$
BEGIN
    IF NEW.parent_id IS NULL THEN
       NEW.parent_id = OLD.parent_id;
    END IF;
    RETURN NEW;
END;
$$;

--
--rollback DROP FUNCTION IF EXISTS db.old_parent_if_null();
--rollback DROP FUNCTION IF EXISTS db.set_parent_if_null();
