--liquibase formatted sql
--

--
--changeset @changeSetAuthor@:@version@ endDelimiter:; failOnError:true logicalFilePath:@logicalFilePath@
--

--
DROP TRIGGER IF EXISTS update_create_time_@schema@_@table@ ON @schema@.@table@;
CREATE TRIGGER update_create_time_@schema@_@table@
    BEFORE INSERT
    ON @schema@.@table@
    FOR EACH ROW
EXECUTE FUNCTION @schema@.update_create_time();

DROP TRIGGER IF EXISTS update_update_time_@schema@_@table@ ON @schema@.@table@;
CREATE TRIGGER update_update_time_@schema@_@table@
    BEFORE UPDATE
    ON @schema@.@table@
    FOR EACH ROW
EXECUTE FUNCTION @schema@.update_update_time();

--
--rollback DROP TRIGGER IF EXISTS update_update_time_@schema@_@table@ ON @schema@.@table@; DROP TRIGGER IF EXISTS update_create_time_@schema@_@table@ ON @schema@.@table@;

