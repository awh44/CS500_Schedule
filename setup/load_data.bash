#!/bin/bash

if [[ $# -ne 3 ]] ; then
	echo Please include hostname, username, and database name to which to connect.
else
	cat create_postgres.sql > tmpfile
	sqlite3 new_courses_500.db '.dump Subjects' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> tmpfile
	sqlite3 new_courses_500.db '.dump Courses_Have' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' | sed 's/,\([0-9][0-9]\),/,'\''0\1'\'',/' | sed 's/,\([0-9]\),/,'\''00\1'\'',/' >> tmpfile
	sqlite3 new_courses_500.db '.dump Terms' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> tmpfile
	sqlite3 new_courses_500.db '.dump Course_Offered_In_Term' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' | sed 's/,\([0-9][0-9]\),/,'\''0\1'\'',/' | sed 's/,\([0-9]\),/,'\''00\1'\'',/' >> tmpfile
	sqlite3 new_courses_500.db '.dump Instructors' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> tmpfile
	sqlite3 new_courses_500.db '.dump Campuses' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> tmpfile
	sqlite3 new_courses_500.db '.dump Sections' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> tmpfile
	sqlite3 new_courses_500.db '.dump TimeBlocks' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> tmpfile
	sqlite3 new_courses_500.db '.dump Meets_At' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> tmpfile
	psql -h $1 -U $2 $3 < tmpfile > /dev/null
	rm tmpfile
fi