CREATE TABLE NewCourses_Have
(
	abbr text,
	num text,
	name text,
	description text,
	credits real,
	subject text not NULL,
	PRIMARY KEY (abbr, num),
	FOREIGN KEY (subject) REFERENCES Subjects(name)
);

INSERT INTO
	NewCourses_Have
SELECT
	abbr text,
	num text,
	name text,
	description text,
	credits real,
	subject text 
FROM
	Courses_Have;

DROP TABLE Courses_Have;
ALTER TABLE NewCourses_Have RENAME TO Courses_Have;

UPDATE
	Courses_Have
SET
	num = '0' || num
WHERE
	num LIKE '__';

UPDATE
	Courses_Have
SET
	num = '00' || num
WHERE
	num LIKE '_';
