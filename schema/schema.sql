CREATE TABLE people
(
    name VARCHAR(30) NOT null,
    ucn serial primary key not null,
    age int not null,
    e_mail VARCHAR(30) not null
);

CREATE TABLE trip
(
    ucn int not null,
    date_of_arrival DATE not null,
    date_of_departure DATE not null,
    city VARCHAR(30) not null
);
ALTER TABLE trip ADD FOREIGN KEY (ucn) REFERENCES people (ucn);