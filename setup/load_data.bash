#!/bin/bash
DATABASE="courses.db"
TMPFILE="tmpfile"

dump_and_filter()
{
	#Dump the sqlite table, take only the insert statements (i.e., not the CREATE TABLE statements),
	#and then remove the quotes from around the table names
	sqlite3 $DATABASE ".dump $1" | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/'
}

dump_and_filter_and_convert()
{
	#The sqlite database currently uses integers in some places it should be using text, so some
	#leading zeros have been removed. Look for the one and two leading zero cases and fix them
	dump_and_filter "$1" | sed 's/,\([0-9][0-9]\),/,'\''0\1'\'',/' | sed 's/,\([0-9]\),/,'\''00\1'\'',/'
}

dump_and_filter_and_write()
{
	dump_and_filter "$1" >> $TMPFILE
}

dump_and_filter_and_convert_and_write()
{
	dump_and_filter_and_convert "$1" >> $TMPFILE
}

if [[ $# -ne 3 ]] ; then
	echo Please include hostname, username, and database name to which to connect.
else
	cat create_postgres.sql > $TMPFILE
	sqlite3 courses.db '.dump Subjects' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> $TMPFILE
	sqlite3 courses.db '.dump Courses_Have' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' | sed 's/,\([0-9][0-9]\),/,'\''0\1'\'',/' | sed 's/,\([0-9]\),/,'\''00\1'\'',/' >> $TMPFILE
	sqlite3 courses.db '.dump Terms' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> $TMPFILE
	sqlite3 courses.db '.dump Course_Offered_In_Term' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' | sed 's/,\([0-9][0-9]\),/,'\''0\1'\'',/' | sed 's/,\([0-9]\),/,'\''00\1'\'',/' >> $TMPFILE
	sqlite3 courses.db '.dump Instructors' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> $TMPFILE
	sqlite3 courses.db '.dump Campuses' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> $TMPFILE
	sqlite3 courses.db '.dump Sections' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> $TMPFILE
	sqlite3 courses.db '.dump TimeBlocks' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> $TMPFILE
	sqlite3 courses.db '.dump Meets_At' | grep 'INSERT' | sed 's/INSERT INTO "\([^"]\+\)"/INSERT INTO \1/' >> $TMPFILE
	psql -h $1 -U $2 $3 < $TMPFILE > /dev/null
	rm $TMPFILE
fi
