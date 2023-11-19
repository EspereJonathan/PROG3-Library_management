CREATE TABLE IF NOT EXISTS visitor (
    visitor_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    reference VARCHAR(255) NOT NULL
);

INSERT INTO visitor (name, reference) VALUES
    ('Visitor 1', 'Reference1'),
    ('Visitor 2', 'Reference2'),
    ('Visitor 3', 'Reference3');