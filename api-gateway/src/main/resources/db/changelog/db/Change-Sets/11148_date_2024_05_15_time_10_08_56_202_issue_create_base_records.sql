--liquibase formatted sql
--

--
--changeset vnsk:11148 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11148_date_2024_05_15_time_10_08_56_202_issue_create_base_records.sql
--

--
CREATE TABLE IF NOT EXISTS db.base_records (
    id            UUID  PRIMARY KEY             NOT NULL    DEFAULT pg_catalog.uuid_generate_v4(),
    parent_id     UUID                          NOT NULL,
      FOREIGN KEY (parent_id)
      REFERENCES  db.base_records (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    type          SMALLINT                      NOT NULL    DEFAULT 0      CHECK (type BETWEEN 0 AND 7),
    user_name     VARCHAR(64)                   NOT NULL,
      CONSTRAINT  FK_605f_db_base_records_security_user_name
      FOREIGN KEY (user_name)
      REFERENCES  security.user_name (user_name)
        ON DELETE CASCADE ON UPDATE CASCADE,
    create_time   TIMESTAMP WITHOUT TIME ZONE   NOT NULL    DEFAULT now(),
    update_time   TIMESTAMP WITHOUT TIME ZONE               DEFAULT now(),
    enabled       BOOLEAN                                   DEFAULT true,
    local_change  BOOLEAN                       NOT NULL    DEFAULT true,
    visible       BOOLEAN                                   DEFAULT true,
    flags         INT                           NOT NULL    DEFAULT 0
    );

--
--rollback DROP TABLE IF EXISTS db.base_records;
