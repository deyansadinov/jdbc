CREATE TABLE articles
(
        id serial PRIMARY KEY NOT NULL,
        title VARCHAR(30) NOT NULL
);
CREATE TABLE articles_history
(
        id serial PRIMARY KEY NOT NULL,
        id_articles int not null,
        title VARCHAR(30) NOT NULL
);


CREATE TRIGGER history_trigger
after UPDATE ON articles FOR EACH ROW EXECUTE PROCEDURE update();

CREATE OR REPLACE FUNCTION update() RETURNS TRIGGER AS $example_table$
    BEGIN
        INSERT INTO articles_history(id_articles, title) VALUES (old.id, old.title);
        RETURN old;
    END;
$example_table$ LANGUAGE plpgsql;