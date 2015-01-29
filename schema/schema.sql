CREATE TABLE articles
(
        id serial PRIMARY KEY NOT NULL,
        title VARCHAR(30) NOT NULL
);
CREATE TABLE articles_history
(
        id serial PRIMARY KEY NOT NULL,
        title VARCHAR(30) NOT NULL
);

--CREATE TRIGGER update_articles_history_trigger
--before UPDATE ON articles  FOR EACH ROW
--insert into articles_history values (old.id,old.title);

CREATE TRIGGER history_trigger
before UPDATE ON articles FOR EACH ROW EXECUTE PROCEDURE update();

CREATE OR REPLACE FUNCTION update() RETURNS TRIGGER AS $example_table$
    BEGIN
        INSERT INTO articles_history(id, title) VALUES (old.id, old.title);
        RETURN NEW;
    END;
$example_table$ LANGUAGE plpgsql;