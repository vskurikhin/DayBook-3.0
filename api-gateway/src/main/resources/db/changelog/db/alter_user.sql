-- This file was last modified at 2021.12.06 18:25 by Victor N. Skurikhin.
-- This is free and unencumbered software released into the public domain.
-- For more information, please refer to <http://unlicense.org>
-- alter_user.sql

GRANT ALL PRIVILEGES ON DATABASE db TO dbuser;
ALTER USER dbuser PASSWORD 'password';
ALTER USER dbuser SUPERUSER;
