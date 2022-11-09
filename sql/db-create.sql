DROP database IF EXISTS testdb;

CREATE database testdb;

USE testdb;

CREATE TABLE users (
	id INT PRIMARY KEY auto_increment,
	login VARCHAR(10) UNIQUE
);

CREATE TABLE teams (
	id INT PRIMARY KEY auto_increment,
	name VARCHAR(10)
);
/*
CREATE TABLE users_teams (
	user_id INT REFERENCES users(id) on delete cascade,
	team_id INT REFERENCES teams(id) on delete cascade,
	UNIQUE (user_id, team_id)
);*/
CREATE TABLE users_teams (
     user_id INT,
     team_id INT,
     UNIQUE (user_id, team_id),
     INDEX `fk_users_teams_users_user_id_idx` (`user_id` ASC),
     INDEX `fk_users_teams_teams_team_id_idx` (`team_id` ASC),
     CONSTRAINT `fk_users_teams_users_user_id`
         FOREIGN KEY (`user_id`)
             REFERENCES `testdb`.`users` (`id`)
             ON DELETE CASCADE
             ON UPDATE NO ACTION,
     CONSTRAINT `fk_users_teams_teams_team_id`
         FOREIGN KEY (`team_id`)
             REFERENCES `testdb`.`teams` (`id`)
             ON DELETE CASCADE
             ON UPDATE NO ACTION
);


INSERT INTO users VALUES (DEFAULT, 'ivanov');
INSERT INTO teams VALUES (DEFAULT, 'teamA');

SELECT * FROM users;
SELECT * FROM teams;
SELECT * FROM users_teams;

