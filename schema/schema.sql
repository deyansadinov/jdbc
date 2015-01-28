CREATE TABLE addresses
(
    id serial PRIMARY KEY NOT NULL,
    address VARCHAR(40) NOT NULL,
    city VARCHAR(20) NOT NULL
);

CREATE TABLE contacts
(
    user_id INT NOT NULL,
    contact_id INT NOT NULL,
    FOREIGN KEY (contact_id) REFERENCES addresses (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE users
(
    id serial PRIMARY KEY NOT NULL,
    name VARCHAR(30) NOT NULL
);