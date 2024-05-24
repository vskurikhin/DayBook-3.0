--liquibase formatted sql
--

--
--changeset vnsk:11160 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11160_date_2024_05_23_time_19_58_52_205_issue_create_json_records.sql
--

--
CREATE TABLE IF NOT EXISTS db.json_records (
    id            UUID  PRIMARY KEY            NOT NULL  DEFAULT pg_catalog.uuid_generate_v4(),
    values        JSONB,
    user_name     VARCHAR(64),
      CONSTRAINT  FK_a412_db_json_records_security_user_name
      FOREIGN KEY (user_name)
      REFERENCES  security.user_name (user_name)
        ON DELETE CASCADE ON UPDATE CASCADE,
    create_time   TIMESTAMP WITHOUT TIME ZONE  NOT NULL   DEFAULT now(),
    update_time   TIMESTAMP WITHOUT TIME ZONE             DEFAULT now(),
    enabled       BOOLEAN                                 DEFAULT true,
    visible       BOOLEAN                                 DEFAULT true,
    flags         INT                          NOT NULL   DEFAULT 0
    );

--
--rollback DROP TABLE IF EXISTS db.json_records;
