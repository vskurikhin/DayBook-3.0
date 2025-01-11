--liquibase formatted sql
--

--
--changeset svn:10004 endDelimiter:$$ splitStatements:false failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/10004_date_2022_12_24_time_22_05_24_issue_60_update_update_time_column.sql
--

--
CREATE OR REPLACE FUNCTION db.update_update_time() RETURNS TRIGGER LANGUAGE plpgsql
AS $$
BEGIN
    NEW.create_time = OLD.create_time;
    NEW.update_time = now();
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION dictionary.update_update_time() RETURNS TRIGGER LANGUAGE plpgsql
AS $$
BEGIN
    NEW.create_time = OLD.create_time;
    NEW.update_time = now();
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION security.update_update_time() RETURNS TRIGGER LANGUAGE plpgsql
AS $$
BEGIN
    NEW.create_time = OLD.create_time;
    NEW.update_time = now();
    RETURN NEW;
END;
$$;

--
--rollback DROP FUNCTION IF EXISTS security.update_update_time(); DROP FUNCTION IF EXISTS dictionary.update_update_time(); DROP FUNCTION IF EXISTS db.update_update_time();
