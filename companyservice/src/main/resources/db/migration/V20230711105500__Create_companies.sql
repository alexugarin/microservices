ALTER TABLE company ALTER COLUMN ogrn TYPE VARCHAR(14);

INSERT INTO company (name, ogrn, description, director_id)
VALUES ('Company1', '11111111111111', 'company 1 for test', 1);
INSERT INTO company (name, ogrn, description, director_id)
VALUES ('Company2', '22222222222222', 'company 2 for test', 2);