--liquibase formatted sql
--

--
--changeset svn:11076 endDelimiter:; failOnError:true logicalFilePath:src/main/resources/db/changelog/db/Change-Sets/11076_date_2023_01_05_time_18_40_08_issue_122_security_user_view.sql
--

--
DROP VIEW IF EXISTS security.user_view;
CREATE OR REPLACE VIEW security.user_view AS
SELECT Usrn.*, RolesOfUser.roles
FROM security.user_name Usrn
         LEFT JOIN LATERAL
    (SELECT u1.user_name,
            coalesce(array_agg(Role.role) FILTER ( WHERE Role.role IS NOT NULL ), '{}')::text[] AS roles
       FROM security.user_name u1
       LEFT JOIN security.user_has_roles uhr
            ON u1.user_name = uhr.user_name
       LEFT JOIN security.role Role
            ON uhr.role = Role.role
      GROUP BY u1.user_name)
             RolesOfUser ON Usrn.user_name = RolesOfUser.user_name;
;

--
--rollback DROP VIEW IF EXISTS security.user_view;
