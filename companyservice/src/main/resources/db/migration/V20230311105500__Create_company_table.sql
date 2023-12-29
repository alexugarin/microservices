CREATE TABLE company
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY
        CONSTRAINT company_pkey
            PRIMARY KEY,
    name        VARCHAR(500) NOT NULL,
    ogrn        INT NOT NULL,
    description TEXT NOT NULL,
    director_id  BIGINT
);