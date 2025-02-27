
CREATE TABLE IF NOT EXISTS recordings (
    id INT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    duration DOUBLE NOT NULL
);

INSERT INTO recordings (id, title, duration) VALUES
(1, 'Mi Primera Grabación', 120.5),
(2, 'Segunda Grabación', 90.0),
(3, 'Tercera Grabación', 60.0);
