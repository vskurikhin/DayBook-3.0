--liquibase formatted sql
--

--
--changeset @changeSetAuthor@:@version@ endDelimiter:; failOnError:true logicalFilePath:@logicalFilePath@
--

--
CREATE SEQUENCE IF NOT EXISTS @schema@.@table@_seq START 1;

CREATE TABLE IF NOT EXISTS @schema@.@table@ (
    id            BIGINT  PRIMARY KEY  NOT NULL  DEFAULT nextval('@schema@.@table@_seq'),
    @key@         VARCHAR(256) NOT NULL UNIQUE,
    @value@       VARCHAR(10485760),
    user_name     VARCHAR(64),
      CONSTRAINT  FK_@rnd@_@schema@_@table@_security_user_name
      FOREIGN KEY (user_name)
      REFERENCES  security.user_name (user_name)
        ON DELETE CASCADE ON UPDATE CASCADE,
    create_time   TIMESTAMP WITHOUT TIME ZONE  NOT NULL   DEFAULT now(),
    update_time   TIMESTAMP WITHOUT TIME ZONE             DEFAULT now(),
    enabled       BOOLEAN                                 DEFAULT false,
    visible       BOOLEAN                                 DEFAULT true,
    flags         INT
    );

--
--rollback DROP TABLE IF EXISTS @schema@.@table@; DROP SEQUENCE IF EXISTS @schema@.@table@_seq;

