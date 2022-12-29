--liquibase formatted sql
--

--
--changeset @changeSetAuthor@:@version@ endDelimiter:; failOnError:true logicalFilePath:@logicalFilePath@
--

--

CREATE INDEX IF NOT EXISTS IDX_@schema@_@table@_user_name
    ON @schema@.@table@ (user_name);

--
--rollback DROP INDEX IF EXISTS @schema@.IDX_@schema@_@table@_user_name;

