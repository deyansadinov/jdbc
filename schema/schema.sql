CREATE TABLE car
(
        id serial PRIMARY KEY NOT NULL,
        power int,
        year int
);

CREATE INDEX index_name on car (power);