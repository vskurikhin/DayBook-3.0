/*
 * This file was last modified at 2022.03.24 13:27 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * words.sql
 * $Id$
 */

CREATE TEMPORARY TABLE temp_word(word VARCHAR(256) PRIMARY KEY NOT NULL);
COPY temp_word FROM '/var/lib/pgsql/words.txt';
SET search_path TO "$user", public, db, dictionary, big_bookings, country_region, security;
INSERT INTO user_name (user_name, password) VALUES ('root', '{noop}'||(SELECT lpad(to_hex((SELECT floor(random() * 2000000000000000000 + 399999999999999999)::bigint)), 16, '0')));
INSERT INTO dictionary.word SELECT word, 'root' AS user_name FROM temp_word;
