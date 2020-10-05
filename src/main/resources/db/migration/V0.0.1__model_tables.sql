-- User
CREATE TABLE application_user (
	id                                   BIGSERIAL PRIMARY KEY,
	email                                TEXT NOT NULL UNIQUE,
	password                             TEXT NOT NULL
);

-- City
CREATE TABLE city (
	id                                   BIGSERIAL PRIMARY KEY,
	name                                 TEXT NOT NULL UNIQUE,
	description                          TEXT NOT NULL,
	population                           INTEGER NOT NULL,
    favourite_count                      INTEGER DEFAULT 0 NOT NULL
);

CREATE INDEX favourite_count_i ON city(favourite_count);
CREATE INDEX city_name_i ON city(name);

-- Favourite user cities
CREATE TABLE user_favourite_city (
	application_user_id                  BIGINT REFERENCES application_user(id),
	city_id                              BIGINT REFERENCES city(id),
	PRIMARY KEY(application_user_id, city_id)
);