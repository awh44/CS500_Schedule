DROP TABLE Subjects CASCADE;
DROP TABLE Courses_Have CASCADE;
DROP TABLE Terms CASCADE;
DROP TABLE Course_Offered_In_Term CASCADE;
DROP TABLE Instructors CASCADE;
DROP TABLE Campuses CASCADE;
DROP TABLE Sections CASCADE;
DROP TABLE TimeBlocks CASCADE;
DROP TABLE Meets_At CASCADE;

CREATE TABLE Subjects
(
	name text PRIMARY KEY
);

CREATE TABLE Courses_Have
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

CREATE TABLE Terms
(
	season text,
	term_type text,
	year integer,
	PRIMARY KEY (season, term_type, year)
);

CREATE TABLE Course_Offered_In_Term
(
	subject text,
	num text,
	season text,
	term_type text,
	year integer,
	PRIMARY KEY (subject, num, season, term_type, year),
	FOREIGN KEY (subject, num) REFERENCES Courses_Have(abbr, num),
	FOREIGN KEY (season, term_type, year) REFERENCES Terms(season, term_type, year)
);

CREATE TABLE Instructors
(
	name text PRIMARY KEY
);

CREATE TABLE Campuses
(
	name text PRIMARY KEY
);

CREATE TABLE Sections
(
	CRN integer,
	subject text not NULL,
	num text not NULL,
	season text not NULL,
	term_type text not NULL,
	year integer not NULL,
	section_id text,
	capacity integer,
	enrolled integer,
	instructor text not NULL,
	campus text not NULL,
	PRIMARY KEY (CRN, subject, num, season, term_type, year),
	FOREIGN KEY (subject, num, season, term_type, year) REFERENCES Course_Offered_In_Term(subject, num, season, term_type, year) ON DELETE CASCADE,
	FOREIGN KEY (instructor) REFERENCES Instructors(name),
	FOREIGN KEY (campus) REFERENCES Campuses(name)
);

CREATE TABLE TimeBlocks
(
	day text,
	start_time time,
	end_time time,
	PRIMARY KEY (day, start_time, end_time)
);

CREATE TABLE Meets_At
(
	CRN integer,
	subject text,
	num text,
	season text,
	term_type text,
	year integer,
	day text,
	start_time time,
	end_time time,
	PRIMARY KEY (CRN, subject, num, season, term_type, year, day, start_time, end_time),
	FOREIGN KEY (CRN, subject, num, season, term_type, year) REFERENCES Sections (CRN, subject, num, season, term_type, year),
	FOREIGN KEY (day, start_time, end_time) REFERENCES TimeBlocks (day, start_time, end_time)
);

