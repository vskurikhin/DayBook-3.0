--liquibase formatted sql
--

--
--changeset svn:10003 endDelimiter:$$ splitStatements:false failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/10003_date_2022_12_24_time_21_59_33_issue_60_update_create_time_column.sql
--

--
CREATE OR REPLACE FUNCTION db.update_create_time() RETURNS TRIGGER LANGUAGE plpgsql
AS $$
BEGIN
    NEW.create_time = now();
    NEW.update_time = NULL;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION dictionary.update_create_time() RETURNS TRIGGER LANGUAGE plpgsql
AS $$
BEGIN
    NEW.create_time = now();
    NEW.update_time = NULL;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION security.update_create_time() RETURNS TRIGGER LANGUAGE plpgsql
AS $$
BEGIN
    NEW.create_time = now();
    NEW.update_time = NULL;
    RETURN NEW;
END;
$$;

--
--rollback DROP FUNCTION IF EXISTS security.update_create_time(); DROP FUNCTION IF EXISTS dictionary.update_create_time(); DROP FUNCTION IF EXISTS db.update_create_time();
