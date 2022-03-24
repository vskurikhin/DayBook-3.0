/*
 * This file was last modified at 2022.03.24 13:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * alter_user.sql
 * $Id$
 */

GRANT ALL PRIVILEGES ON DATABASE dbuser TO db;
ALTER USER dbuser PASSWORD 'password';
ALTER USER dbuser SUPERUSER;
