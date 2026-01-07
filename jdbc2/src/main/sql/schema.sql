CREATE TYPE continents AS ENUM ('AFRICA', 'EUROPA', 'ASIA', 'AMERICA');
CREATE TABLE Team (                                              id SERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    continent continents NOT NULL
);

CREATE TYPE player_position AS ENUM ('GK', 'DEF', 'MIDF', 'STR');
CREATE TABLE Player (                                            id SERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    age INT NOT NULL,
    position player_position NOT NULL,
    team_id INT REFERENCES Team(id)
);

ALTER TABLE player ADD COLUMN goal_nb INT NULL;
UPDATE player SET goal_nb = 0 WHERE goal_nb IS NULL;

UPDATE player SET goal_nb = 0 WHERE name = 'Thibaut Courtois';
UPDATE player SET goal_nb = 2 WHERE name = 'Dani Carvajal';
UPDATE player SET goal_nb = 5 WHERE name = 'Jude Bellingham';
UPDATE player SET goal_nb = NULL WHERE name = 'Robert Lewandowski';
UPDATE player SET goal_nb = NULL WHERE name = 'Anotine Griezmannn';