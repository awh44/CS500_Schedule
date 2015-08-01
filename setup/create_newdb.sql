DROP TABLE Subjects;
DROP TABLE Courses_Have;
DROP TABLE Sections;
DROP TABLE Instructors;
DROP TABLE Campuses;
DROP TABLE Terms;
DROP TABLE TimeBlocks;
DROP TABLE Meets_At;
DROP TABLE Course_Offered_In_Term;

CREATE TABLE Subjects
(
	name text PRIMARY KEY
);

CREATE TABLE Courses_Have
(
	abbr text,
	num integer,
	name text,
	description text,
	credits real,
	subject text not NULL,
	PRIMARY KEY (abbr, num),
	FOREIGN KEY (subject) REFERENCES Subjects(name)
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
	FOREIGN KEY (subject, num, season, term_type, year) REFERENCES Course_Offered_In_Term(subject, num, season,
	term_type, year),
	FOREIGN KEY (instructor) REFERENCES Instructors(name),
	FOREIGN KEY (campus) REFERENCES Campuses(name)
);

CREATE TABLE Instructors
(
	name text PRIMARY KEY
);

CREATE TABLE Terms
(
	season text,
	term_type text,
	year integer,
	PRIMARY KEY (season, term_type, year)
);

CREATE TABLE Campuses
(
	name text PRIMARY KEY
);

INSERT INTO Campuses(name) VALUES("University City");
INSERT INTO Campuses(name) VALUES("Burlington County College");
INSERT INTO Campuses(name) VALUES("Sacramento");
INSERT INTO Campuses(name) VALUES("Center City");
INSERT INTO Campuses(name) VALUES("Online");
INSERT INTO Campuses(name) VALUES("PTS");

CREATE TABLE TimeBlocks
(
	day text,
	start_time text,
	end_time text,
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
    start_time text,
    end_time text,
    PRIMARY KEY (CRN, subject, num, season, term_type, year, day, start_time, end_time),
    FOREIGN KEY (CRN, subject, num, season, term_type, year) REFERENCES Sections (CRN, subject, num, season, term_type,
year),
    FOREIGN KEY (day, start_time, end_time) REFERENCES TimeBlocks (day, start_time, end_time)
);

CREATE TABLE Course_Offered_In_Term
(
	subject text,
	num integer,
	season text,
	term_type text,
	year integer,
	PRIMARY KEY (subject, num, season, term_type, year),
	FOREIGN KEY (subject, num) REFERENCES Courses_Have(abbr, num),
	FOREIGN KEY (season, term_type, year) REFERENCES Terms(season, term_type, year)
);
