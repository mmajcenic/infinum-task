-- User
CREATE TABLE application_user (
	id                                   BIGSERIAL PRIMARY KEY,
	email                                TEXT NOT NULL UNIQUE,
	password                             TEXT NOT NULL,
	created_at                           TIMESTAMP WITH TIME ZONE NOT NULL,
    last_modified_at                     TIMESTAMP WITH TIME ZONE NOT NULL
);

-- City
CREATE TABLE city (
	id                                   BIGSERIAL PRIMARY KEY,
	name                                 TEXT NOT NULL UNIQUE,
	description                          TEXT NOT NULL,
	population                           INTEGER NOT NULL,
    favourite_count                      INTEGER DEFAULT 0,
    created_at                           TIMESTAMP WITH TIME ZONE NOT NULL,
    last_modified_at                     TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX city_name_i ON city(name);

-- Favourite user cities
CREATE TABLE user_favourite_city (
	application_user_id                  BIGINT REFERENCES application_user(id),
	city_id                              BIGINT REFERENCES city(id),
	PRIMARY KEY(application_user_id, city_id)
);

INSERT INTO city(name, description, population, created_at, last_modified_at) VALUES('Zagreb', 'Capital city of Croatia', 100000, current_timestamp, current_timestamp);
INSERT INTO city(name, description, population, created_at, last_modified_at) VALUES('Ljubljana', 'Capital city of Slovenia', 200000, current_timestamp, current_timestamp);
INSERT INTO city(name, description, population, created_at, last_modified_at) VALUES('Rome', 'Capital city of Italy', 300000, current_timestamp, current_timestamp);
INSERT INTO city(name, description, population, created_at, last_modified_at) VALUES('Vienna', 'Capital city of Austria', 400000, current_timestamp, current_timestamp);
INSERT INTO city(name, description, population, created_at, last_modified_at) VALUES('Budapest', 'Capital city of Hungary', 500000, current_timestamp, current_timestamp);
INSERT INTO city(name, description, population, created_at, last_modified_at) VALUES('Berlin', 'Capital city of Germany', 600000, current_timestamp, current_timestamp);
INSERT INTO city(name, description, population, created_at, last_modified_at) VALUES('Prague', 'Capital city of Czechia', 700000, current_timestamp, current_timestamp);
INSERT INTO city(name, description, population, created_at, last_modified_at) VALUES('Bratislava', 'Capital city of Slovakia', 800000, current_timestamp, current_timestamp);
INSERT INTO city(name, description, population, created_at, last_modified_at) VALUES('Paris', 'Capital city of France', 900000, current_timestamp, current_timestamp);
INSERT INTO city(name, description, population, created_at, last_modified_at) VALUES('Athens', 'Capital city of Greece', 1000000, current_timestamp, current_timestamp);